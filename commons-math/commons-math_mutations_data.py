import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    cov_type = "BRANCH"

    # orthogonal_tests = [1, 2, 3, 4, 5, 6, 7, 8]
    # orthogonal_data = do_runs("orthogonal", orthogonal_tests, "-Dtest=Vector3D_orthogonalTest#", "testOrthogonal",
    #     "mutation_matrix/mutations.xml", cov_type, start_line=261, end_line=280, mute_print=False)
    # generate_mutations_killed_bar_chart(orthogonal_data, "% Killed in Vector3D.orthogonal()", cov_type)

    gcd_tests = [1, 2, 3, 4, 5, 6, 7]
    gcd_data = do_runs("gcd", gcd_tests, "-Dtest=MathUtils_gcdTest#", "testGcd",
        "mutation_matrix/mutations.xml", cov_type, start_line=400, end_line=451, mute_print=False)
    generate_mutations_killed_bar_chart(gcd_data, "% Killed in MathUtils.gcd()", cov_type)
    

if __name__ == "__main__":
    main()
