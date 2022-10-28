import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    classify_tests = [2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 16, 17, 18, 20, 21]
    do_runs("classify", classify_tests, "-Dtest=TriangleTest#", "testClassify", 
        "mutation_matrix/mutations.xml", "BRANCH", mute_print=False)


if __name__ == "__main__":
    main()
