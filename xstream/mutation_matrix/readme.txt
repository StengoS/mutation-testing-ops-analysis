The mutations.xml file has been manually modified due to timed-out mutations not having any associated killing tests
with them, despite us counting timed-out tests as "killed". We added to the <killingTests> field by doing the following:
1. Generate the mutation matrix/mutations.xml (named 'mm1') with the entirety of the test suite.
2. Comment out all test cases in the test suite except for one + generate mutation matrix (named 'mm2') with info on 
   just that one test case.
3. For all mutations in mm2 that are timed-out or killed and also timed-out in mm1, add the isolated test case to the 
   <killingTests> field of the corresponding mutations in mm1 (following the naming/format of rest of matrix).
4. Repeat steps 2 and 3 until all test cases in the test suite have been ran individually. mm2 will be remade each time.
5. Use mm1 as the mutation matrix for generating mutation data.