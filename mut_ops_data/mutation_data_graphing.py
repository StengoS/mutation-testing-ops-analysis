import json
import pandas
import matplotlib.pyplot as plt
import numpy as np


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
    copied_in_data = {
        "run_id": 5,
        "method_name": "sqrt",
        "coverage_type": "INSTRUCTION",
        "total_test_cases": 4,
        "test_suite_strength": 0.25,
        "mutations_not_killed": {
            "ConditionalsBoundaryMutator": 1,
            "InlineConstantMutator": 1,
            "MathMutator": 1
        },
        "mutations_killed": {
            "MemberVariableMutator": 2,
            "InlineConstantMutator": 4,
            "MathMutator": 7,
            "NegateConditionalsMutator": 3,
            "NonVoidMethodCallMutator": 1,
            "RemoveConditionalMutator_ORDER_ELSE": 3,
            "RemoveConditionalMutator_ORDER_IF": 2,
            "ArgumentPropagationMutator": 1,
            "PrimitiveReturnsMutator": 1
        },
        "total_mutations": 27,
        "test_cases_picked": [
            2
        ],
        "test_cases_not_picked": [
            4,
            5,
            1
        ]
    }

    generate_mutations_combined_bar_chart(copied_in_data, "% Killed and Survived in Bisect.sqrt()", "INSTRUCTION")


if __name__ == "__main__":
    main()