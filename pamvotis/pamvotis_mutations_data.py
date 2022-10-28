import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    addnode_tests = [1, 2, 3, 4, 5, 6, 7, 8]
    do_runs("addNode", addnode_tests, "-Dtest=SimulatorAddNodeTest#", "testAddNode",
        "mutation_matrix/mutations.xml", start_line=1412, end_line=1466, mute_print=False)

    removenode_tests = [1, 2, 3]
    do_runs("removeNode", removenode_tests, "-Dtest=SimulatorRemoveNodeTest#", "testRemoveNode",
        "mutation_matrix/mutations.xml", start_line=1468, end_line=1486, mute_print=False)
    

if __name__ == "__main__":
    main()
