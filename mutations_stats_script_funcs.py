import pandas
import random
import re
import subprocess
import xml.etree.ElementTree as ET


def gen_random_test_case_order(original_list):
    """
    Given a list of test cases represented as integers, create and return a deep copy of
    the list but randomly shuffled.
    """
    ran_list = original_list.copy()
    random.shuffle(ran_list)

    print("Test case list shuffled: \n" + str(ran_list))
    return ran_list


def clean_target_dir():
    """
    Removes/resets the 'target' directory so that conditional coverage testing can be
    ran again and updated.
    """
    subprocess.run(["mvn", "clean"], shell=True, stdout=subprocess.DEVNULL)


def check_coverage_xml(jacoco_xml_path, method_name, mute_print):
    tree = ET.parse(jacoco_xml_path)
    root = tree.getroot()

    for method in root.iter("method"):
        if method.get("name") == method_name:
            for counter in method:
                if counter.get("type") == "INSTRUCTION":
                    missed_instructions = int(counter.get("missed"))
    
    if not mute_print:
        print("Instructions missed: " + str(missed_instructions))
    if missed_instructions == 0:
        return True

    return False


"""
Until conditional coverage at 100%, picks the next test case in the shuffled test case list and
runs Jacoco coverage tool with all tests in test_cases_picked. Resets and makes a new shuffled
test case list if it ends up picking all original test cases.
"""
def get_cases_full_conditional_cov(mvn_test_cases_flag, unit_test_name, test_cases_original, 
        method_name, shuffled_cases, mute_print=True):
    mvn_flag_original = mvn_test_cases_flag
    test_cases_picked = []
    conditional_cov_met = False

    while conditional_cov_met is not True:
        clean_target_dir()

        next_test_case = shuffled_cases.pop(0)
        test_cases_picked.append(next_test_case)
        mvn_test_cases_flag += unit_test_name + str(next_test_case)

        subprocess.run(["mvn", "jacoco:prepare-agent", "test", "install", \
            "jacoco:report", mvn_test_cases_flag], shell=True, stdout=subprocess.DEVNULL)
        
        if not mute_print:
            print("Current list of picked test cases: \n" + str(test_cases_picked))

        if check_coverage_xml("target/site/jacoco/jacoco.xml", method_name, mute_print) is True:

            # Resets if picked test case list's length is equal to original test case list's length
            if len(test_cases_picked) == len(test_cases_original):
                if not mute_print:
                    print("Resetting and generating new list...")
                test_cases_picked = []
                shuffled_cases = gen_random_test_case_order(test_cases_original)
                mvn_test_cases_flag = mvn_flag_original

            else:
                conditional_cov_met = True

        elif conditional_cov_met is not True:
            mvn_test_cases_flag += "+"

    return (test_cases_picked, shuffled_cases)


"""
Parses through a mutation matrix in XML format (mutations.xml) and given a list of
test cases that were randomly picked to get to 100% conditional coverage, returns a
list of mutations that weren't killed (killing tests not included in the input list
of test cases).
"""
def parse_mutation_matrix(matrix_file_path, test_cases_picked, mute_print=True,
        start_line=-1, end_line=-1):
    mutations_not_killed = {}
    mutations_killed = {}

    tree = ET.parse(matrix_file_path)
    root = tree.getroot()

    for mutation in root.findall("mutation"):
        if mutation.get("status") == "KILLED":
            mutator_txt = mutation.find("mutator").text
            line_number_txt = mutation.find("lineNumber").text
            killing_tests_txt = mutation.find("killingTests").text

            line_number = int(line_number_txt)
            if start_line > -1 and end_line > -1:
                if line_number < start_line or line_number > end_line:
                    continue

            mutator = mutator_txt.split(".")[-1]
            killing_tests = list(map(int, re.findall("\d+", killing_tests_txt)))

            matching_in_lists = set(killing_tests) & set(test_cases_picked)

            if len(matching_in_lists) == 0:
                if not mute_print:
                    print((line_number, mutator))
                    print(killing_tests)
                    print("--------")

                if mutator in mutations_not_killed:
                    mutations_not_killed[mutator] += 1
                else:
                    mutations_not_killed[mutator] = 1
            else:
                if mutator in mutations_killed:
                    mutations_killed[mutator] += 1
                else:
                    mutations_killed[mutator] = 1
    

    return (mutations_not_killed, mutations_killed)
