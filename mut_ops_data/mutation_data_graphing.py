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
   "run_id": 3,
   "method_name": "decodeName",
   "coverage_type": "INSTRUCTION",
   "total_test_cases": 6,
   "test_suite_strength": 0.8333333333333334,
   "mutations_not_killed": {
      "NegateConditionalsMutator": 1
   },
   "mutations_killed": {
      "ConditionalsBoundaryMutator": 2,
      "ConstructorCallMutator": 1,
      "InlineConstantMutator": 8,
      "MathMutator": 5,
      "NegateConditionalsMutator": 11,
      "NonVoidMethodCallMutator": 15,
      "RemoveConditionalMutator_EQUAL_ELSE": 7,
      "RemoveConditionalMutator_EQUAL_IF": 4,
      "RemoveConditionalMutator_ORDER_ELSE": 2,
      "RemoveConditionalMutator_ORDER_IF": 2,
      "ArgumentPropagationMutator": 1,
      "NakedReceiverMutator": 5,
      "EmptyObjectReturnValsMutator": 1
   },
   "total_mutations": 65,
   "test_cases_picked": [
      1,
      4,
      2,
      3,
      5
   ],
   "test_cases_not_picked": [
      6
   ]
}

    generate_mutations_combined_bar_chart(copied_in_data, "% Killed and Survived in XmlFriendlyNameCoder.decodeName()", "INSTRUCTION")


if __name__ == "__main__":
    main()