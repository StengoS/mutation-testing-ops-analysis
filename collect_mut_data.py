from distutils.dir_util import copy_tree
from os import listdir
from os.path import isfile, join

import os
import subprocess
import csv
import json


column_names = ['file', 'class', 'method', 'constructor', 'line', 'cbo', 'cboModified', 
                'fanin', 'fanout', 'wmc', 'rfc', 'loc', 'returnsQty', 'variablesQty', 
                'parametersQty', 'methodsInvokedQty', 'methodsInvokedLocalQty', 
                'methodsInvokedIndirectLocalQty', 'loopQty', 'comparisonsQty', 'tryCatchQty', 
                'parenthesizedExpsQty', 'stringLiteralsQty', 'numbersQty', 'assignmentsQty', 
                'mathOperationsQty', 'maxNestedBlocksQty', 'anonymousClassesQty', 'innerClassesQty', 
                'lambdasQty', 'uniqueWordsQty', 'modifiers', 'logStatementsQty', 'hasJavaDoc']

used_columns = ["line", "wmc", "rfc", "loc", "returnsQty", "variablesQty", "parametersQty", 
                "methodsInvokedQty", 'loopQty', 'comparisonsQty', 'tryCatchQty', 
                'parenthesizedExpsQty', 'stringLiteralsQty', 'numbersQty', 'assignmentsQty', 
                'mathOperationsQty', 'maxNestedBlocksQty', "uniqueWordsQty"]

all_mut_ops_used = ['MemberVariableMutator', 'InlineConstantMutator', 'MathMutator', 'NegateConditionalsMutator', 
                    'NonVoidMethodCallMutator', 'RemoveConditionalMutator_ORDER_ELSE', 'RemoveConditionalMutator_ORDER_IF', 
                    'ArgumentPropagationMutator', 'PrimitiveReturnsMutator', 'ConditionalsBoundaryMutator', 
                    'ConstructorCallMutator', 'RemoveConditionalMutator_EQUAL_ELSE', 'RemoveConditionalMutator_EQUAL_IF', 
                    'NakedReceiverMutator', 'EmptyObjectReturnValsMutator', 'VoidMethodCallMutator', 'NullReturnValsMutator', 
                    'IncrementsMutator', 'RemoveIncrementsMutator', 'InvertNegsMutator', 'RemoveSwitchMutator_1', 
                    'RemoveSwitchMutator_2', 'SwitchMutator', 'BooleanFalseReturnValsMutator', 'BooleanTrueReturnValsMutator']

grouped_mutators = {
    "arithmetics": ["IncrementsMutator", "MathMutator", "RemoveIncrementsMutator", "InvertNegsMutator"],
    "conditionals": ["ConditionalsBoundaryMutator", "NegateConditionalsMutator", "RemoveConditionalMutator_EQUAL_IF", 
                     "RemoveConditionalMutator_EQUAL_ELSE", "RemoveConditionalMutator_ORDER_IF", "RemoveSwitchMutator_0",
                     "RemoveConditionalMutator_ORDER_ELSE", "RemoveSwitchMutator_1", "RemoveSwitchMutator_2", "SwitchMutator"],
    "variables": ["ArgumentPropagationMutator", "InlineConstantMutator", "MemberVariableMutator", "NullReturnValsMutator", 
                  "PrimitiveReturnsMutator", "BooleanFalseReturnValsMutator", "BooleanTrueReturnValsMutator"],
    "objects/methods": ["ConstructorCallMutator", "EmptyObjectReturnValsMutator", "NonVoidMethodCallMutator", 
                        "VoidMethodCallMutator", "NakedReceiverMutator"],
}

mutator_group_pairings = {}
for group in grouped_mutators:
    for mutator in grouped_mutators[group]:
        mutator_group_pairings[mutator] = group



## In big-num-##
repo_name = "big-num-arithmetic-<repo_name>-main"
anon_id = "F##_ID#"
mut_data_dict = {
    "repo_name": repo_name,
    "anon_id": anon_id,
    "mutated_funcs": {},
    "coverage_info": {},
}


# Add necessary libs to lib/, see lib-jars-used (at root of repo)
copy_tree("../lib-jars-used", repo_name + "/lib")


# Add `bignum` package to all files in src/ (comment out if already ran before)
# java_src_files = [file for file in listdir(repo_name + "/src/") if isfile(join(repo_name + "/src/", file))]

# for src_file in java_src_files:
#     file_path = repo_name + "/src/" + src_file
#     file = open(file_path, "r")
#     file_content = file.read()
#     file.close()

#     new_file = open(file_path, "w")
#     new_file.write("package bignum;\n\n" + file_content)
#     new_file.close()

# Compile src/ to target/
subprocess.run(["javac", "-cp", "lib/*", "-d", "target", "src/*"], 
    shell=True, cwd=repo_name + "/")

if os.path.exists(repo_name + "/target/bignum/SampleTest.class"):
    os.remove(repo_name + "/target/bignum/SampleTest.class")


# Run Pitest in cmdline on project, generates mutations.csv in report/
subprocess.run(
    ["java", "-cp", "lib/*", "org.pitest.mutationtest.commandline.MutationCoverageReport", 
        "--reportDir", "report",
        "--classPath", "target/",
        "--targetClasses", "bignum.*",
        "--targetTests", "bignum*",
        "--sourceDirs", "src/",
        "--outputFormats", "csv,html"], 
    shell=True, cwd=repo_name + "/")


# Get test execution data using the JaCoCo agent in a JUnit5 run
subprocess.run(
    ["java", "-javaagent:lib/jacocoagent.jar", "-jar", "lib/junit-platform-console-standalone-1.9.2.jar", 
        "--class-path", "target/", 
        "--scan-class-path"],
    shell=True, cwd=repo_name + "/")


# Get conditional coverage data using JaCoCo CLI and test execution data
subprocess.run(
    ["java", "-jar", "lib/jacococli.jar", "report", "jacoco.exec", 
        "--classfiles", "target/bignum",
        "--csv", "report/cov_info.csv"],
    shell=True, cwd=repo_name + "/")


# Parse through report/cov_info.csv and add to mut_data_dict
with open(repo_name + "/report/cov_info.csv", "r") as csv_file:
    reader = csv.reader(csv_file)

    for row in reader:
        if row[0] == "GROUP":
            continue

        class_name = row[2]
        if "Test" in class_name:
            continue

        mut_data_dict["coverage_info"][class_name] = {
                "instruction_coverage": 0.0, 
                "branch_coverage": 0.0,
            }
        
        instruction_missed = float(row[3])
        instruction_covered = float(row[4])
        instruction_total = instruction_missed + instruction_covered
        branch_missed = float(row[5])
        branch_covered = float(row[6])
        branch_total = branch_missed + branch_covered

        curr_class = mut_data_dict["coverage_info"][class_name]

        if instruction_total == 0:
            curr_class["instruction_coverage"] = 100.0
        else:
            curr_class["instruction_coverage"] = instruction_covered / instruction_total * 100.0
        if branch_total == 0:
            curr_class["branch_coverage"] = 100.0
        else:
            curr_class["branch_coverage"] = branch_covered / branch_total * 100.0


# Run CK in cmdline on project, generates class.csv and method.csv in report/
# Using "ck-0.7.1-SNAPSHOT-jar-with-dependencies-CUSTOM.jar" that adds ALL conditionals
subprocess.run(["java", "-jar", "../ck-0.7.1-SNAPSHOT-jar-with-dependencies-CUSTOM.jar", 
        "src/", "false", "0", "false", "report/"], shell=True, cwd=repo_name + "/")


# Convert data from individual method to mutation data; mut_data.json file output
# [ <class name>.java (0), bignum.<class name> (1), <mutation operator> (2), <func name> (3), 
#   <line num> (4), <mutation status> (5), <killing test case if 'KILLED'> (6) ]
# <mutation status> options = "KILLED", "SURVIVED", "TIMED_OUT", "NO_COVERAGE"
with open(repo_name + "/report/mutations.csv", "r") as csv_file:
    reader = csv.reader(csv_file)

    for row in reader:
        class_name = row[0].split(".")[0]
        func_name = class_name + "." + row[3]
        mut_status = row[5]
        mut_op = row[2].split(".")[-1]

        # Filters out mutation testing done on test case files
        if "Test" in class_name or "FileProcessor" in class_name:
            continue

        mutated_funcs = mut_data_dict["mutated_funcs"]
        if func_name not in mutated_funcs:
            mutated_funcs[func_name] = {
                "func_name": func_name,
                "total_mutations": 1,
                "mutations_killed": {},
                "mutations_not_killed": {},
            }
        else:
            mutated_funcs[func_name]["total_mutations"] += 1
        
        func_info = mutated_funcs[func_name]

        if mut_status == "KILLED" or mut_status == "TIMED_OUT":
            if mut_op not in func_info["mutations_killed"]:
                func_info["mutations_killed"][mut_op] = 1
            else:
                func_info["mutations_killed"][mut_op] += 1

        elif mut_status == "SURVIVED" or mut_status == "NO_COVERAGE":
            if mut_op not in func_info["mutations_not_killed"]:
                func_info["mutations_not_killed"][mut_op] = 1
            else:
                func_info["mutations_not_killed"][mut_op] += 1


# Parse through report/method.csv to match code metrics data to the functions
with open(repo_name + "/report/method.csv", "r") as csv_file:
    reader = csv.reader(csv_file)

    for row in reader:
        class_name = row[1].split(".")[-1]
        method_name = row[2].split("/")[0]
        mut_func_name = class_name + "." + method_name
        
        if mut_func_name in mut_data_dict["mutated_funcs"]:
            ck_code_metrics = {}
            for column in used_columns:
                index = column_names.index(column)
                ck_code_metrics[column] = row[index]
            mut_data_dict["mutated_funcs"][mut_func_name]["ck_code_metrics"] = ck_code_metrics


# Groups up mutations_killed and mutations_not_killed into the mutator groupings
mutated_funcs_info = mut_data_dict["mutated_funcs"]
for curr_func in mutated_funcs_info:
    curr_info = mutated_funcs_info[curr_func]
    curr_info["mutation_groupings_killed"] = {"arithmetics": 0, "conditionals": 0, 
                                              "variables": 0, "objects/methods": 0}
    curr_info["mutation_groupings_not_killed"] = {"arithmetics": 0, "conditionals": 0, 
                                                  "variables": 0, "objects/methods": 0}
    
    for mutator in curr_info["mutations_killed"]:
        grouping = mutator_group_pairings[mutator]
        curr_info["mutation_groupings_killed"][grouping] += curr_info["mutations_killed"][mutator]
    for mutator in curr_info["mutations_not_killed"]:
        grouping = mutator_group_pairings[mutator]
        curr_info["mutation_groupings_not_killed"][grouping] += curr_info["mutations_not_killed"][mutator]


# Converts mut_data_dict into a .json file
mut_data_json = json.dumps(mut_data_dict, indent = 4)
print(mut_data_json)
with open(anon_id + "_mut_data.json", "w") as outfile:
    json.dump(mut_data_dict, outfile)


"""
JSON DATA TEMPLATE FOR EACH RUN
{
    "repo_name": String,
    "id": String (anonymized from repo_name),
    "coverage_info": 
        {
            <class_name1>:
                {
                    "instruction_coverage": float,
                    "branch_coverage": float,
                },
            <class_name2>:
                {
                    ...
                },
        },
    "mutated_functions": 
        {
            <func_name1>: 
                {
                    "func_name": String,
                    "total_mutations": int,
                    "mutations_killed": {},
                    "mutations_not_killed": {},
                    "ck_code_metrics": {},
                    "mutation_groupings_killed": {},
                    "mutation_groupings_not_killed": {}
                },
            <func_name2>:
                {
                    ...
                },
        }
}
"""
## See kintis_methods_mut_data_instruction.py for Kintis-ver format
