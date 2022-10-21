import json
import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    runs = []
    for run_id in range(1, 6):
        run_data = {
            "run_id": run_id,
            "total_test_cases": 0,
            "test_suite_strength": 0.0,
            "mutations_not_killed": {},
            "mutations_killed": {},
            "test_cases_picked": [],
            "test_cases_not_picked": []
        }

        # Randomize ordering of test cases for addNode() in SimulatorAddNodeTest.java
        test_cases_original = [1, 2, 3, 4, 5, 6, 7, 8]
        shuffled_cases = gen_random_test_case_order(test_cases_original)

        # Build test case list, one test at a time, until conditional coverage at 100%
        # addNode() test cases' file name: SimulatorAddNodeTest; Unit test name: testAddNode<#>
        test_suite_info = get_cases_full_conditional_cov("-Dtest=SimulatorAddNodeTest#", "testAddNode", \
            test_cases_original, "addNode", shuffled_cases, mute_print=False)
        test_cases_picked = test_suite_info[0]

        run_data["test_cases_picked"] = test_cases_picked
        run_data["test_cases_not_picked"] = test_suite_info[1]
        run_data["test_suite_strength"] = len(test_cases_picked) / len(test_cases_original)
        run_data["total_test_cases"] = len(test_cases_original)

        print("Final test case list: \n" + str(test_cases_picked))

        # Parses through mutation matrix XML with list of picked test cases to find what
        # mutations were not killed
        mutations_info = parse_mutation_matrix("mutation_matrix/mutations.xml", test_cases_picked,
            mute_print=False, start_line=1412, end_line=1466)

        run_data["mutations_not_killed"] = mutations_info[0]
        run_data["mutations_killed"] = mutations_info[1]
        # for mutation in mutations_not_killed:
        #    print(mutation)
        
        runs.append(run_data)

        # Prints out run_data in JSON format
        run_data_json = json.dumps(run_data, indent=3)
        print(run_data_json)

    # Prints out runs in JSON format
    runs_json = json.dumps(runs, indent=3)
    print(runs_json)

    runs.sort(key=lambda run: run["test_suite_strength"])

    print("=======================================")
    print("Median run is: ")
    median_run_json = json.dumps(runs[2], indent=3)
    print(median_run_json)

    

if __name__ == "__main__":
    main()
