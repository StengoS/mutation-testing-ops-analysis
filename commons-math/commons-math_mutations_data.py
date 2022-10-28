import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    orthogonal_tests = [1, 2, 3, 4, 5, 6, 7, 8]
    do_runs("orthogonal", orthogonal_tests, "-Dtest=Vector3D_orthogonalTest#", "testOrthogonal",
        "mutation_matrix/mutations.xml", start_line=262, end_line=279, mute_print=False)
    

if __name__ == "__main__":
    main()
