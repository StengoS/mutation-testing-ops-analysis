import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
        cov_type = "BRANCH"

        # capitalize_tests = [1, 2, 3, 4, 5, 6]
        # capitalize_data = do_runs("capitalize", capitalize_tests, "-Dtest=WordUtils_capitalizeTest#", "testCapitalize", 
        #     "mutation_matrix/mutations.xml", cov_type, start_line=71, end_line=96, mute_print=False)
        # generate_mutations_killed_bar_chart(capitalize_data, "% Killed in WordUtils.capitalize()", cov_type)
        
        # lastindexof_tests = [1, 2, 3, 4, 5, 6, 7, 8]
        # lastindexof_data = do_runs("lastIndexOf", lastindexof_tests, "-Dtest=ArrayUtils_lastIndexOfTest#", "testLastIndexOf",
        #     "mutation_matrix/mutations.xml", cov_type, start_line=1277, end_line=1302, mute_print=False, 
        #     method_line_num="1279")
        # generate_mutations_killed_bar_chart(lastindexof_data, "% Killed in ArrayUtils.lastIndexOf()", cov_type)
        
        # subarray_tests = [1, 2, 3, 4]
        # subarray_data = do_runs("subarray", subarray_tests, "-Dtest=ArrayUtils_subarrayTest#", "testSubarray", 
        #     "mutation_matrix/mutations.xml", cov_type, start_line=440, end_line=460, mute_print=False,
        #     method_line_num="442")
        # generate_mutations_killed_bar_chart(subarray_data, "% Killed in ArrayUtils.subarray()", cov_type)

        # tomap_tests = [1, 2, 3, 4, 5]
        # tomap_data = do_runs("toMap", tomap_tests, "-Dtest=ArrayUtils_toMapTest#", "testToMap", 
        #     "mutation_matrix/mutations.xml", cov_type, start_line=235, end_line=262, mute_print=False)
        # generate_mutations_killed_bar_chart(tomap_data, "% Killed in ArrayUtils.toMap()", cov_type)

        wrap_tests = [1, 2, 3, 4, 5, 6]
        wrap_data = do_runs("wrap", wrap_tests, "-Dtest=WordUtils_wrapTest#", "testWrap",
          "mutation_matrix/mutations.xml", cov_type, start_line=15, end_line=65, mute_print=False)
        generate_mutations_killed_bar_chart(wrap_data, "% Killed in WordUtils.wrap()", cov_type)
        

if __name__ == "__main__":
    main()
