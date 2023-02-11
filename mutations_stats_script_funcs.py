import json
import pandas
import matplotlib.pyplot as plt
import random
import re
import subprocess
import xml.etree.ElementTree as ET


def gen_random_test_case_order(original_list):
    """
    Given a list of test cases represented as integers, create and return a deep copy of
    the list but randomly shuffled.

    Args:
        - original_list {list} - all test cases associated with a method
    Returns: 
        - ran_list {list} - randomized test cases, to be ran individually, left-to-right
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


def check_mut_op_coverage(cov_type, start_line, end_line, shuffled_cases, test_cases_original, mute_print=True):
    """
    Incrementally checks if the randomized list of test cases reaches the specified coverage type. 
    While checking, a subset of the full original list of test cases is built, and will reset if the
    subset length equals the original list length. If a randomized subset list of test cases meets
    the coverage type while having a length less than the original list length, then the process 
    completes and the randomized subset is returned along with the full randomized list.

    This version is for '1-OP' cov_type, as the process looks at the mutation matrix and tests if a
    subset list of test cases kills all RemoveConditionalOperator's.

    Args:
        - cov_type {string} - hardcoded string specifying coverage type; supports '1-OP'
        - start_line {int} - first line of code of method being tested
        - end_line {int} - last line of code of method being tested
        - shuffled_cases {list} - randomized test cases of test_cases_original
        - test_cases_original {list} - all test cases associated with a method
        - mute_print {boolean} - specifies whether to print to output progress of checking, default to True
    Returns:
        - (test_cases_picked, shuffled_cases) {tuple}
            -  test_cases_picked {list} - subset of randomized list of test cases that meets specified coverage
            - shuffled_cases {list} - randomized test cases of test_cases_original
    """
    test_cases_picked = []
    op_cov_met = False

    while op_cov_met is not True:
        next_test_case = shuffled_cases.pop(0)
        test_cases_picked.append(next_test_case)

        if not mute_print:
            print("Current list of picked test cases: \n" + str(test_cases_picked))

        tree = ET.parse("mutation_matrix/mutations.xml")
        root = tree.getroot() 

        op_cov_met = True
        for mutation in root.findall("mutation"):
            if mutation.get("status") == "KILLED" or mutation.get("status") == "TIMED_OUT":

                mutator_txt = mutation.find("mutator").text
                line_number_txt = mutation.find("lineNumber").text
                killing_tests_txt = mutation.find("killingTests").text

                line_number = int(line_number_txt)
                if start_line > -1 and end_line > -1:
                    if line_number < start_line or line_number > end_line:
                        continue

                mutator = mutator_txt.split(".")[-1]
                if cov_type == "1-OP":
                    one_op_list = ["RemoveConditionalMutator_EQUAL_ELSE", "RemoveConditionalMutator_EQUAL_IF",
                        "RemoveConditionalMutator_ORDER_ELSE", "RemoveConditionalMutator_ORDER_IF"]
                    if mutator not in one_op_list:
                        continue

                # 2-OP not currently supported due to AOD not being a valid mutation operator in PIT
                # elif cov_type == "2-OP":
                #     two_op_list = []
                #     if mutator not in two_op_list:
                #         continue

                killing_tests = list(map(int, re.findall("\d+", killing_tests_txt)))

                matching_in_lists = set(killing_tests) & set(test_cases_picked)

                if len(matching_in_lists) == 0:
                    op_cov_met = False
                    if not mute_print:
                        print("\tTests not killed: " + str(killing_tests))
                        print("\t" + mutator + ", " + str(line_number))
                    break
        
        if len(test_cases_picked) == len(test_cases_original):
            if not mute_print:
                print("Resetting and generating new list...")
            test_cases_picked = []
            shuffled_cases = gen_random_test_case_order(test_cases_original)
            op_cov_met = False

    return (test_cases_picked, shuffled_cases)


def check_coverage_xml(jacoco_xml_path, method_name, mute_print, line_num, cov_type):
    """

    """
    tree = ET.parse(jacoco_xml_path)
    root = tree.getroot()

    if cov_type == "1-OP" or cov_type == "2-OP":
        return check_mut_op_coverage(cov_type)
    else:
        missed = -1
        for method in root.iter("method"):
            if method.get("name") == method_name and (line_num == "-1" or method.get("line") == line_num):
                for counter in method:
                    if cov_type == "INSTRUCTION" and counter.get("type") == "INSTRUCTION":
                        missed = int(counter.get("missed"))
                    elif cov_type == "BRANCH" and counter.get("type") == "BRANCH":
                        missed = int(counter.get("missed"))
    
        if not mute_print:
            print("Instructions missed: " + str(missed))
        if missed == 0:
            return True

    return False


def get_cases_full_conditional_cov(mvn_test_cases_flag, unit_test_name, test_cases_original, 
        method_name, shuffled_cases, cov_type, mute_print=True, line_num="-1"):
    """
    Incrementally checks if the randomized list of test cases reaches the specified coverage type. 
    While checking, a subset of the full original list of test cases is built, and will reset if the
    subset length equals the original list length. If a randomized subset list of test cases meets
    the coverage type while having a length less than the original list length, then the process 
    completes and the randomized subset is returned along with the full randomized list.

    This version is for 'BRANCH' and 'INSTRUCTION' cov_type. 
    For example, until conditional coverage at 100%, picks the next test case in the shuffled test 
    case list and runs Jacoco coverage tool with all tests in test_cases_picked. Resets and makes 
    a new shuffled test case list if it ends up picking all original test cases.

    Args:
        - 
    Returns:
        - 
    """
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

        if check_coverage_xml("target/site/jacoco/jacoco.xml", method_name, mute_print, 
            line_num, cov_type) is True:

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
    total_mutations = 0

    tree = ET.parse(matrix_file_path)
    root = tree.getroot()

    for mutation in root.findall("mutation"):
        if mutation.get("status") == "KILLED" or mutation.get("status") == "TIMED_OUT":

            mutator_txt = mutation.find("mutator").text
            line_number_txt = mutation.find("lineNumber").text
            killing_tests_txt = mutation.find("killingTests").text

            line_number = int(line_number_txt)
            if start_line > -1 and end_line > -1:
                if line_number < start_line or line_number > end_line:
                    continue
            
            total_mutations += 1

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
    

    return (mutations_not_killed, mutations_killed, total_mutations)


def do_runs(method_name, test_cases_arr, file_name_mvn, test_case_name, 
    mut_matrix_file, cov_type, start_line=-1, end_line=-1, mute_print=False, method_line_num="-1"):
    """
    Primary function to be used elsewhere for generating percentage of mutations killed/not killed
    in subset lists of test cases that meet a specified coverage type (assessing strength of subsets
    of the full test suite, e.g., imitating a developer stopping unit testing when they have met
    100% conditional coverage then assessing that imitated test suite). This does five complete runs
    and selects the run with the median test_suite_strength across all runs.

    Args:
        -
    Returns:
        - 
    """
    runs = []
    for run_id in range(1, 6):
        run_data = {
            "run_id": run_id,
            "method_name": method_name,
            "coverage_type": cov_type,
            "total_test_cases": 0,
            "test_suite_strength": 0.0,
            "mutations_not_killed": {},
            "mutations_killed": {},
            "total_mutations": 0,
            "test_cases_picked": [],
            "test_cases_not_picked": []
        }
    
        test_cases_original = test_cases_arr
        shuffled_cases = gen_random_test_case_order(test_cases_original)

        if cov_type == "1-OP" or cov_type == "2-OP":
            test_suite_info = check_mut_op_coverage(cov_type, start_line, end_line, shuffled_cases, test_cases_original,
                mute_print=mute_print)

        elif cov_type == "INSTRUCTION" or cov_type == "BRANCH":
            test_suite_info = get_cases_full_conditional_cov(file_name_mvn, test_case_name, 
                test_cases_original, method_name, shuffled_cases, cov_type, mute_print, line_num=method_line_num)
        
        else:
            print("Invalid coverage type.")
            return

        test_cases_picked = test_suite_info[0]

        run_data["test_cases_picked"] = test_cases_picked
        run_data["test_cases_not_picked"] = test_suite_info[1]
        run_data["test_suite_strength"] = len(test_cases_picked) / len(test_cases_original)
        run_data["total_test_cases"] = len(test_cases_original)

        print("Final test case list: \n" + str(test_cases_picked))

        mutations_info = parse_mutation_matrix(mut_matrix_file, test_cases_picked,
            mute_print=mute_print, start_line=start_line, end_line=end_line)

        run_data["mutations_not_killed"] = mutations_info[0]
        run_data["mutations_killed"] = mutations_info[1]
        run_data["total_mutations"] = mutations_info[2]

        runs.append(run_data)

        run_data_json = json.dumps(run_data, indent=3)
        print(run_data_json)
    
    cov_data_txt = open(cov_type + "_cov_data.txt", "w")

    runs_json = json.dumps(runs, indent=3)
    cov_data_txt.write(runs_json)

    runs.sort(key=lambda run: run["test_suite_strength"])

    
    cov_data_txt.write("=======================================")
    cov_data_txt.write("Median run is: ")
    median_run_json = json.dumps(runs[2], indent=3)
    cov_data_txt.write(median_run_json)

    cov_data_txt.close()
    return (runs[2])


def generate_mutations_killed_bar_chart(run_data, graph_title, cov_type):
    """
    
    """
    mutations_killed = run_data["mutations_killed"]
    total_mutations = run_data["total_mutations"]

    graph_data = {
        "Mutations": [],
        "% Killed": []
    }

    for mutation in mutations_killed:
        graph_data["Mutations"].append(mutation)
        graph_data["% Killed"].append(mutations_killed[mutation] / total_mutations)

    df = pandas.DataFrame(data=graph_data)

    df.plot.bar(x="Mutations", y="% Killed", rot=70, title=graph_title)
    plt.show()
    # plt.savefig(cov_type + "_cov_graph_percent_killed.png")

    plt.close()
