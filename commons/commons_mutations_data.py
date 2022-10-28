import sys
sys.path.append("..")
from mutations_stats_script_funcs import *


def main():
        # capitalize_tests = [1, 2, 3, 4, 5, 6]
        # do_runs("capitalize", capitalize_tests, "-Dtest=WordUtils_capitalizeTest#", "testCapitalize", 
        #     "mutation_matrix/mutations.xml", start_line=71, end_line=96, mute_print=False)
        
        # lastindexof_tests = [1, 2, 3, 4, 5, 6, 7, 8]
        # do_runs("lastIndexOf", lastindexof_tests, "-Dtest=ArrayUtils_lastIndexOfTest#", "testLastIndexOf",
        #     "mutation_matrix/mutations.xml", start_line=1277, end_line=1302, mute_print=False, 
        #     method_line_num="1279")
        
        # subarray_tests = [1, 2, 3, 4]
        # do_runs("subarray", subarray_tests, "-Dtest=ArrayUtils_subarrayTest#", "testSubarray", 
        #     "mutation_matrix/mutations.xml", start_line=440, end_line=460, mute_print=False,
        #     method_line_num="442")

        tomap_tests = [1, 2, 3, 4, 5]
        do_runs("toMap", tomap_tests, "-Dtest=ArrayUtils_toMapTest#", "testToMap", 
            "mutation_matrix/mutations.xml", start_line=235, end_line=262, mute_print=False)
        

    

if __name__ == "__main__":
    main()
