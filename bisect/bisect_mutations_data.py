import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    # Not able to get branch coverage, had 1 branch missing
    # Looking further into it, specific branch will always be missing because of how source code is written
    # TODO: Write up further details on what's going on
    cov_type = "BRANCH"
    sqrt_tests = [1, 2, 4, 5]
    sqrt_data = do_runs("sqrt", sqrt_tests, "-Dtest=BisectTest#", "testBisect", 
        "mutation_matrix/mutations.xml", cov_type, mute_print=False)
    generate_mutations_killed_bar_chart(sqrt_data, "% Killed in Bisect.sqrt()", cov_type)


if __name__ == "__main__":
    main()
