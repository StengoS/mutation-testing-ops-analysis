import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
    cov_type = "INSTRUCTION"

    decodename_tests = [1, 2, 3, 4, 5, 6]
    decodename_data = do_runs("decodeName", decodename_tests, "-Dtest=DecodeNameTest#", "testDecodeName",
        "mutation_matrix/mutations.xml", cov_type, start_line=145, end_line=198, mute_print=False)
    generate_mutations_killed_bar_chart(decodename_data, "% Killed in XmlFriendlyNameCoder.decodeName()", cov_type)
    

if __name__ == "__main__":
    main()
