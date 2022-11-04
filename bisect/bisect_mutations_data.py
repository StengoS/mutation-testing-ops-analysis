import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    # Not able to get branch coverage, had 1 branch missing (need to write test case?)
    cov_type = "INSTRUCTION"
    sqrt_tests = [1, 2, 4, 5]
    sqrt_data = do_runs("sqrt", sqrt_tests, "-Dtest=BisectTest#", "testBisect", 
        "mutation_matrix/mutations.xml", cov_type, mute_print=False)
    generate_mutations_killed_bar_chart(sqrt_data, "% Killed in Bisect.sqrt()", cov_type)


if __name__ == "__main__":
    main()
