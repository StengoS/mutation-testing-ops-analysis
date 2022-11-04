import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    cov_type = "BRANCH"

    # addnode_tests = [1, 2, 3, 4, 5, 6, 7, 8]
    # addnode_data = do_runs("addNode", addnode_tests, "-Dtest=SimulatorAddNodeTest#", "testAddNode",
    #     "mutation_matrix/mutations.xml", cov_type, start_line=1412, end_line=1466, mute_print=False)
    # generate_mutations_killed_bar_chart(addnode_data, "% Killed in Simulator.addNode()", cov_type)

    removenode_tests = [1, 2, 3]
    removenode_data = do_runs("removeNode", removenode_tests, "-Dtest=SimulatorRemoveNodeTest#", "testRemoveNode",
        "mutation_matrix/mutations.xml", cov_type, start_line=1468, end_line=1486, mute_print=False)
    generate_mutations_killed_bar_chart(removenode_data, "% Killed in Simulator.removeNode()", cov_type)
    

if __name__ == "__main__":
    main()
