from distutils.dir_util import copy_tree
from os import listdir
from os.path import isfile, join

import os
import subprocess
import csv
import json


## In big-num-<quarter>
repo_name = "big-num-arithmetic-###-main"
anon_id = "###_##"
mut_data_dict = {
    "repo_name": repo_name,
    "anon_id": anon_id,
    "mutated_funcs": {},
}

# Add necessary libs to lib/, see pit-lib (at root of repo)
copy_tree("../pit-lib", repo_name + "/lib")

# Add `bignum` package to all files in src/
# Comment out if already ran before
java_src_files = [file for file in listdir(repo_name + "/src/") if isfile(join(repo_name + "/src/", file))]

for src_file in java_src_files:
    file_path = repo_name + "/src/" + src_file
    file = open(file_path, "r")
    file_content = file.read()
    file.close()

    new_file = open(file_path, "w")
    new_file.write("package bignum;\n\n" + file_content)
    new_file.close()

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
        "--outputFormats", "csv"], 
    shell=True, cwd=repo_name + "/")

# Run CK in cmdline on project, generates class.csv and method.csv in report/
subprocess.run(["java", "-jar", "../ck-0.7.1-SNAPSHOT-jar-with-dependencies.jar", 
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
    "mutated_functions": 
        {
            <func_name1>: 
                {
                    "func_name": String,
                    "total_mutations": int,
                    "mutations_killed": {},
                    "mutations_not_killed": {},
                    "ck_code_metrics": {}
                },
            <func_name2>:
                {
                    ...
                },
        }
}
"""
## See kintis_methods_mut_data_instruction.py for Kintis-ver format