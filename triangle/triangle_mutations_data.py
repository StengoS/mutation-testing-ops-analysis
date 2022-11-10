import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    cov_type = "1-OP"
    classify_tests = [2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 16, 17, 18, 20, 21]
    classify_data = do_runs("classify", classify_tests, "-Dtest=TriangleTest#", "testClassify", 
        "mutation_matrix/mutations.xml", cov_type, mute_print=False)
    generate_mutations_killed_bar_chart(classify_data, "% Killed in Triangle.classify()", cov_type)


if __name__ == "__main__":
    main()
