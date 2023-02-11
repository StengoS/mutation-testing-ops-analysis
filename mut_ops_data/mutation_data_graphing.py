import json
import pandas
import matplotlib.pyplot as plt
import numpy as np

"""
Used to generate bar graphs of mutations killed/not killed data, written in style of
throw-away code
TODO: Get JSON of individual methods into .json file that can be more easily worked with
TODO: Create way to quickly take data from .json file and graph, as modular as possible
"""

def generate_mutations_combined_bar_chart(run_data, graph_title, cov_type):
    mutations_survived = run_data["mutations_not_killed"]
    mutations_killed = run_data["mutations_killed"]
    total_mutations = run_data["total_mutations"]

    # <mutation name> : [<percent killed>, <percent survived>]
    mutations = {}

    for mutation in mutations_killed:
        mutations[mutation] = [mutations_killed[mutation] / total_mutations, 0.0]
    
    for mutation in mutations_survived:
        if mutation in mutations:
            mutations[mutation][1] = mutations_survived[mutation] / total_mutations
        else:
            mutations[mutation] = [0.0, mutations_survived[mutation] / total_mutations]
    
    print(mutations)

    x_mutations = []
    y_mutations_killed = []
    z_mutations_survived = []

    for mutation in mutations:
        x_mutations.append(mutation)
        y_mutations_killed.append(mutations[mutation][0])
        z_mutations_survived.append(mutations[mutation][1])
    
    x_axis = np.arange(len(mutations))

    plt.bar(x_axis - 0.2, y_mutations_killed, 0.4, label = "% Killed")
    plt.bar(x_axis + 0.2, z_mutations_survived, 0.4, label = "% Survived")

    plt.xticks(x_axis, x_mutations, rotation=70)
    plt.xlabel("Mutations")
    plt.ylabel("% Killed or Survived")
    plt.title(graph_title)
    plt.legend()
    plt.show()

    plt.close()


def main():
    # generate_mutations_combined_bar_chart(copied_in_data, 
    # "% Killed and Survived in XmlFriendlyNameCoder.decodeName()", "INSTRUCTION")

    # data structure - <mutation name> : [<toMap>, <subarray>, <lastIndexOf>, <addNode>, <removeNode>]
    # % killed
    tomap_data = {
   "run_id": 3,
   "method_name": "toMap",
   "coverage_type": "INSTRUCTION",
   "total_test_cases": 5,
   "test_suite_strength": 0.8,
   "mutations_not_killed": {
      "InlineConstantMutator": 2,
      "NonVoidMethodCallMutator": 1,
      "ArgumentPropagationMutator": 1
   },
   "mutations_killed": {
      "ConditionalsBoundaryMutator": 2,
      "ConstructorCallMutator": 5,
      "InlineConstantMutator": 2,
      "NegateConditionalsMutator": 5,
      "NonVoidMethodCallMutator": 15,
      "RemoveConditionalMutator_EQUAL_ELSE": 3,
      "RemoveConditionalMutator_EQUAL_IF": 3,
      "RemoveConditionalMutator_ORDER_ELSE": 2,
      "RemoveConditionalMutator_ORDER_IF": 2,
      "ArgumentPropagationMutator": 1,
      "NakedReceiverMutator": 10,
      "EmptyObjectReturnValsMutator": 2
   },
   "total_mutations": 56,
   "test_cases_picked": [
      4,
      5,
      3,
      1
   ],
   "test_cases_not_picked": [
      2
   ]
}

    subarray_data = {
   "run_id": 3,
   "method_name": "subarray",
   "coverage_type": "INSTRUCTION",
   "total_test_cases": 4,
   "test_suite_strength": 0.75,
   "mutations_not_killed": {
      "InlineConstantMutator": 1,
      "RemoveConditionalMutator_ORDER_IF": 1
   },
   "mutations_killed": {
      "InlineConstantMutator": 2,
      "MathMutator": 1,
      "NegateConditionalsMutator": 4,
      "NonVoidMethodCallMutator": 4,
      "RemoveConditionalMutator_EQUAL_ELSE": 1,
      "RemoveConditionalMutator_EQUAL_IF": 1,
      "RemoveConditionalMutator_ORDER_ELSE": 3,
      "RemoveConditionalMutator_ORDER_IF": 2,
      "VoidMethodCallMutator": 1,
      "NakedReceiverMutator": 1,
      "NullReturnValsMutator": 2
   },
   "total_mutations": 24,
   "test_cases_picked": [
      2,
      1,
      3
   ],
   "test_cases_not_picked": [
      4
   ]
}

    lastindexof_data = {
   "run_id": 5,
   "method_name": "lastIndexOf",
   "coverage_type": "INSTRUCTION",
   "total_test_cases": 8,
   "test_suite_strength": 0.75,
   "mutations_not_killed": {
      "ConditionalsBoundaryMutator": 2,
      "PrimitiveReturnsMutator": 1
   },
   "mutations_killed": {
      "ConditionalsBoundaryMutator": 2,
      "InlineConstantMutator": 4,
      "MathMutator": 1,
      "NegateConditionalsMutator": 8,
      "NonVoidMethodCallMutator": 1,
      "RemoveConditionalMutator_EQUAL_ELSE": 4,
      "RemoveConditionalMutator_EQUAL_IF": 4,
      "RemoveConditionalMutator_ORDER_ELSE": 3,
      "RemoveConditionalMutator_ORDER_IF": 4,
      "PrimitiveReturnsMutator": 4
   },
   "total_mutations": 38,
   "test_cases_picked": [
      4,
      1,
      6,
      7,
      5,
      3
   ],
   "test_cases_not_picked": [
      2,
      8
   ]
}

    addnode_data = {
   "run_id": 4,
   "method_name": "addNode",
   "coverage_type": "INSTRUCTION",
   "total_test_cases": 8,
   "test_suite_strength": 0.5,
   "mutations_not_killed": {
      "InlineConstantMutator": 1,
      "MathMutator": 7,
      "NonVoidMethodCallMutator": 1,
      "MemberVariableMutator": 1,
      "NakedReceiverMutator": 3,
      "RemoveSwitchMutator_0": 1
   },
   "mutations_killed": {
      "ConditionalsBoundaryMutator": 1,
      "ConstructorCallMutator": 3,
      "InlineConstantMutator": 3,
      "MathMutator": 10,
      "NegateConditionalsMutator": 3,
      "NonVoidMethodCallMutator": 5,
      "RemoveConditionalMutator_EQUAL_ELSE": 2,
      "RemoveConditionalMutator_EQUAL_IF": 2,
      "RemoveConditionalMutator_ORDER_ELSE": 1,
      "RemoveConditionalMutator_ORDER_IF": 1,
      "VoidMethodCallMutator": 2,
      "MemberVariableMutator": 1,
      "RemoveSwitchMutator_1": 1,
      "RemoveSwitchMutator_2": 1,
      "SwitchMutator": 1
   },
   "total_mutations": 51,
   "test_cases_picked": [
      6,
      4,
      7,
      3
   ],
   "test_cases_not_picked": [
      2,
      1,
      5,
      8
   ]
}

    removenode_data = {
   "run_id": 3,
   "method_name": "removeNode",
   "coverage_type": "INSTRUCTION",
   "total_test_cases": 3,
   "test_suite_strength": 0.6666666666666666,
   "mutations_not_killed": {
      "InlineConstantMutator": 2,
      "VoidMethodCallMutator": 1,
      "BooleanFalseReturnValsMutator": 1
   },
   "mutations_killed": {
      "ConditionalsBoundaryMutator": 1,
      "ConstructorCallMutator": 2,
      "InlineConstantMutator": 3,
      "MathMutator": 1,
      "NegateConditionalsMutator": 3,
      "NonVoidMethodCallMutator": 6,
      "RemoveConditionalMutator_EQUAL_ELSE": 2,
      "RemoveConditionalMutator_EQUAL_IF": 2,
      "RemoveConditionalMutator_ORDER_ELSE": 1,
      "RemoveConditionalMutator_ORDER_IF": 1,
      "MemberVariableMutator": 1,
      "NakedReceiverMutator": 3
   },
   "total_mutations": 30,
   "test_cases_picked": [
      2,
      1
   ],
   "test_cases_not_picked": [
      3
   ]
}

    mutations = {}

    # for mutation in gcd_data["mutations_not_killed"]:
    #     mutations[mutation] = [gcd_data["mutations_not_killed"][mutation] / gcd_data["total_mutations"], 0.0, 0.0, 0.0]
    
    # for mutation in orthogonal_data["mutations_not_killed"]:
    #     if mutation in mutations:
    #         mutations[mutation][1] = orthogonal_data["mutations_not_killed"][mutation] / orthogonal_data["total_mutations"]
    #     else:
    #         mutations[mutation] = [0.0, orthogonal_data["mutations_not_killed"][mutation] / orthogonal_data["total_mutations"], 0.0, 0.0]
    
    # for mutation in classify_data["mutations_not_killed"]:
    #     if mutation in mutations:
    #         mutations[mutation][2] = classify_data["mutations_not_killed"][mutation] / classify_data["total_mutations"]
    #     else:
    #         mutations[mutation] = [0.0, 0.0, classify_data["mutations_not_killed"][mutation] / classify_data["total_mutations"], 0.0]
    
    # for mutation in sqrt_data["mutations_not_killed"]:
    #     if mutation in mutations:
    #         mutations[mutation][3] = sqrt_data["mutations_not_killed"][mutation] / sqrt_data["total_mutations"]
    #     else:
    #         mutations[mutation] = [0.0, 0.0, 0.0, sqrt_data["mutations_not_killed"][mutation] / sqrt_data["total_mutations"]]

    # print(mutations)

    # n = 7
    # ind = np.arange(n)
    # width = 0.15

    # x_mutations = []
    # y_mutations_killed_gcd = []
    # z_mutations_killed_orthogonal = []
    # a_mutations_killed_classify = []
    # b_mutations_killed_sqrt = []

    # for mutation in mutations:
    #     x_mutations.append(mutation)
    #     y_mutations_killed_gcd.append(mutations[mutation][0])
    #     z_mutations_killed_orthogonal.append(mutations[mutation][1])
    #     a_mutations_killed_classify.append(mutations[mutation][2])
    #     b_mutations_killed_sqrt.append(mutations[mutation][3]) 

    # plt.bar(ind, y_mutations_killed_gcd, width, label = "gcd()")
    # plt.bar(ind + width, z_mutations_killed_orthogonal, width, label = "orthogonal()")
    # plt.bar(ind + width * 2, a_mutations_killed_classify, width, label = "classify()")
    # plt.bar(ind + width * 3, b_mutations_killed_sqrt, width, label = "sqrt()")

    # plt.xticks(ind + width, x_mutations, rotation=90)
    # plt.xlabel("Mutations")
    # plt.ylabel("% Survived")
    # plt.title("% Survived in Math Methods")
    # plt.legend()
    # plt.show()

    # plt.close()

    for mutation in tomap_data["mutations_not_killed"]:
        mutations[mutation] = [tomap_data["mutations_not_killed"][mutation] / tomap_data["total_mutations"], 0.0, 0.0, 0.0, 0.0]
    
    for mutation in subarray_data["mutations_not_killed"]:
        if mutation in mutations:
            mutations[mutation][1] = subarray_data["mutations_not_killed"][mutation] / subarray_data["total_mutations"]
        else:
            mutations[mutation] = [0.0, subarray_data["mutations_not_killed"][mutation] / subarray_data["total_mutations"], 0.0, 0.0, 0.0]
    
    for mutation in lastindexof_data["mutations_not_killed"]:
        if mutation in mutations:
            mutations[mutation][2] = lastindexof_data["mutations_not_killed"][mutation] / lastindexof_data["total_mutations"]
        else:
            mutations[mutation] = [0.0, 0.0, lastindexof_data["mutations_not_killed"][mutation] / lastindexof_data["total_mutations"], 0.0, 0.0]
    
    for mutation in addnode_data["mutations_not_killed"]:
        if mutation in mutations:
            mutations[mutation][3] = addnode_data["mutations_not_killed"][mutation] / addnode_data["total_mutations"]
        else:
            mutations[mutation] = [0.0, 0.0, 0.0, addnode_data["mutations_not_killed"][mutation] / addnode_data["total_mutations"], 0.0]
    
    for mutation in removenode_data["mutations_not_killed"]:
        if mutation in mutations:
            mutations[mutation][4] = removenode_data["mutations_not_killed"][mutation] / removenode_data["total_mutations"]
        else:
            mutations[mutation] = [0.0, 0.0, 0.0, 0.0, removenode_data["mutations_not_killed"][mutation] / removenode_data["total_mutations"]]


    print(mutations)

    n = 12
    ind = np.arange(n)
    width = 0.16

    x_mutations = []
    y_mutations_killed_tomap = []
    z_mutations_killed_subarray = []
    a_mutations_killed_lastindexof = []
    b_mutations_killed_addnode = []
    c_mutations_killed_removenode = []

    for mutation in mutations:
        x_mutations.append(mutation)
        y_mutations_killed_tomap.append(mutations[mutation][0])
        z_mutations_killed_subarray.append(mutations[mutation][1])
        a_mutations_killed_lastindexof.append(mutations[mutation][2])
        b_mutations_killed_addnode.append(mutations[mutation][3])
        c_mutations_killed_removenode.append(mutations[mutation][4])

    plt.bar(ind, y_mutations_killed_tomap, width, label = "toMap()")
    plt.bar(ind + width, z_mutations_killed_subarray, width, label = "subarray()")
    plt.bar(ind + width * 2, a_mutations_killed_lastindexof, width, label = "lastIndexOf()")
    plt.bar(ind + width * 3, b_mutations_killed_addnode, width, label = "addNode()")
    plt.bar(ind + width * 4, c_mutations_killed_removenode, width, label = "removeNode()")

    plt.xticks(ind + width * 2, x_mutations, rotation=90)
    plt.xlabel("Mutations")
    plt.ylabel("% Survived")
    plt.title("% Survived in Data Structure Methods")
    plt.legend()
    plt.show()

    plt.close()
    

if __name__ == "__main__":
    main()