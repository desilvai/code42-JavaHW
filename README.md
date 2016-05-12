code42-JavaHW
=============
Code42 Interview Code


Build Instructions
------------------
* To build using gradle, execute the following (from the directory 
  containing `build.gradle`):
  
      gradle wrapper
      ./gradlew build
      
  This was tested using gradle version 1.4 on Ubuntu 14.04 LTS.  This 
  will configure the gradle wrapper so we are using a consistent gradle 
  version, build the jar file (`build/libs/code42-1.0.jar`) for the 
  exercises, and run all JUnit tests.  The output of the JUnit tests can
  be found in `build/reports/tests/index.html`.  To build on Windows,
  replace `./gradlew` with `gradle.bat`.
      

Testing: Exercise 1, The Input Analyzer
---------------------------------------
To execute a non-JUnit test, you can use the included test driver.  Run:
`java -classpath build/libs/code42-1.0.jar com.code42.inputAnalysis.InputAnalysisDriver <pathToFile> [stringToFind1] [stringToFind2] ...`
where 
* `pathToFile` is the absolute or relative path to the file to 
  analyze (do not use "..", ".", wildcards, or other path elements that 
  are normally processed by the shell in the path string),
* `stringToFind1`...`stringToFindN` (optional) are the strings to find 
  in the parsed file (case sensitive).


### Manual Test Executions
Included are some test executions, showing both the command that was run
and expected output.  You can run these same commands from the top-level 
directory of the program distribution and should see the same results.  
Note that this was tested on Ubuntu 14.04 LTS from bash.

The test input files for this set of tests can be found in 
`src/test/resources/fileProcessor`.  In this directory, there are a 
number of test input files:
  * emptySampleInput.txt -- an empty file
  * givenSampleInput.txt -- a file containing the example text from the
      assignment description
  * overflowDouble.txt -- a file in which the numbers will cause the
      double to overflow
  * negativeNumbers.txt -- a file containing signed numbers and at least
      one number that is in scientific notation and one with a leading 0
  * sumTest.txt -- a file that contains numbers whose sum will have more than
      two decimal plances

#### Test 1: Invalid File
This checks that an exception is thrown if we attempt to analyze a 
non-existent file (bad input).

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.inputAnalysis.InputAnalysisDriver src/test/resources/fileProcessor/emptySample
    Error encountered running the analysis.  Error details below:
    
    java.io.FileNotFoundException: src/test/resources/fileProcessor/emptySample (No such file or directory)
        at java.io.FileInputStream.open0(Native Method)
        at java.io.FileInputStream.open(FileInputStream.java:195)
        at java.io.FileInputStream.<init>(FileInputStream.java:138)
        at java.io.FileReader.<init>(FileReader.java:72)
        at com.code42.inputAnalysis.FileProcessor.<init>(FileProcessor.java:108)
        at com.code42.inputAnalysis.InputAnalysisDriver.main(InputAnalysisDriver.java:47)
    
    
    Terminating test driver...
    
    
#### Test 2: Directory
This checks that an exception is thrown if we attempt to analyze a 
directory (another form of bad input).

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.inputAnalysis.InputAnalysisDriver src/test/resources/fileProcessor
    Error encountered running the analysis.  Error details below:
    
    java.io.FileNotFoundException: src/test/resources/fileProcessor (Is a directory)
        at java.io.FileInputStream.open0(Native Method)
        at java.io.FileInputStream.open(FileInputStream.java:195)
        at java.io.FileInputStream.<init>(FileInputStream.java:138)
        at java.io.FileReader.<init>(FileReader.java:72)
        at com.code42.inputAnalysis.FileProcessor.<init>(FileProcessor.java:108)
        at com.code42.inputAnalysis.InputAnalysisDriver.main(InputAnalysisDriver.java:47)
    
    
    Terminating test driver...


#### Test 3: Empty File, No String Search
Checks that if we are given an empty file, that the sum is 0, the 
average is undefined (since we cannot divide by 0), the median is none 
(since there is no median of an empty set), the percent lines in file 
that are numbers is undefined (again, divide by 0), and the set of 
non-numeric strings in the file is empty.

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.inputAnalysis.InputAnalysisDriver src/test/resources/fileProcessor/emptySampleInput.txt 
    Printed String:
      Sum of Numbers: 0.00
      Average of Numbers: UNDEFINED
      Median of Numbers: NONE
      Percent of lines that are numbers: UNDEFINED (no lines parsed)
      Non-numeric strings in file (with count): NONE
    
    
    Sum: 0.0
    Number of Numbers: 0
    
    
    Done!
    
   
#### Test 3: Empty File, With String Search
Does the same check as in Test 2, but it also attempts to find some
strings in the file.  They should all return false.

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.inputAnalysis.InputAnalysisDriver src/test/resources/fileProcessor/emptySampleInput.txt someString1 someString2 foo
    Printed String:
      Sum of Numbers: 0.00
      Average of Numbers: UNDEFINED
      Median of Numbers: NONE
      Percent of lines that are numbers: UNDEFINED (no lines parsed)
      Non-numeric strings in file (with count): NONE
    
    
    Sum: 0.0
    Number of Numbers: 0
    
    
    Strings Present?
      "someString1":  false
      "someString2":  false
      "foo":  false
    
    
    Done!

   

#### Test 4: Provided Sample Input, With String Search
Checks that the provided test input gives us the numbers we expect 
(happy path test) and that we are able to find a string that was present
in the file. 

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.inputAnalysis.InputAnalysisDriver src/test/resources/fileProcessor/givenSampleInput.txt someString1 someString2 foo
     Printed String:
       Sum of Numbers: 16.20
       Average of Numbers: 5.40
       Median of Numbers: 5.00
       Percent of lines that are numbers: 42.86
       Non-numeric strings in file (with count):
         The quick brown fox:1
         jumped over the lazy dog.:1
         foo:2
     
     
     Sum: 16.2
     Number of Numbers: 3
     
     
     Strings Present?
       "someString1":  false
       "someString2":  false
       "foo":  true
     
     
     Done!


#### Test 5: Double Overflow
This test case checks a number of things.  First, it checks that we can 
overflow a double without an issue.  Second, when we overflow the 
double, we can convert it back to a double correctly (as Infinity).  
Third, we check that "0x555" gets treated as a string and not a number.
Fourth, we check that MAX_DOUBLE is not included as a string present in 
the file.

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.inputAnalysis.InputAnalysisDriver src/test/resources/fileProcessor/overflowDouble.txt 0x555 1.7976931348623157E308
    Printed String:
      Sum of Numbers: 1258385194403620990000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.00
      Average of Numbers: 179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.00
      Median of Numbers: 179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.00
      Percent of lines that are numbers: 70.00
      Non-numeric strings in file (with count):
        this is a realy large string unvnerivneirnvai inQPOM NVOIMP OUDVNWIeOJ I  WENWOIEVWIEMc ebiwebouneoeva .e weeoi !#@^ jvenro 6 ^# 7372294:1
        aaaaaaaaa:1
        0x555:1
    
    
    Sum: Infinity
    Number of Numbers: 7
    
    
    Strings Present?
      "0x555":  true
      "1.7976931348623157E308":  false
    
    
    Done!

Note, this was checked using the calculator tool in Linux (it did some 
rounding, but it seemed correct otherwise) and gedit to count the 
columns.

#### Test 6: Signed Numbers
We need to check the format of numbers that are acceptable, including 
specifying the sign or using scientific notation.  Further, we might as 
well check for empty strings in the file.

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.inputAnalysis.InputAnalysisDriver src/test/resources/fileProcessor/negativeNumbers.txt "" " "
    Printed String:
      Sum of Numbers: -99680.90
      Average of Numbers: -14240.13
      Median of Numbers: -0.05
      Percent of lines that are numbers: 77.78
      Non-numeric strings in file (with count):
        :2
    
    
    Sum: -99680.9
    Number of Numbers: 7
    
    
    Strings Present?
      "":  true
      " ":  false
    
    
    Done!
    
This found an issue with the division operation when computing the 
arithmetic mean.  It turns out that we needed to ensure there was some 
rounding during the division because it would continue to compute the 
decimal value for an infinite number of places (or when the JVM decided 
to throw an ArithmeticException).


#### Test 7: Sum Precision
We want to check that the getTotal() function returns a value that has been 
rounded to 2 decimal places.

    >java -classpath build/libs/code42-1.0.jar  com.code42.inputAnalysis.InputAnalysisDriver src/test/resources/fileProcessor/sumTest.txt "" " "
    Printed String:
      Sum of Numbers: 6.07
      Average of Numbers: 2.02
      Median of Numbers: 2.02
      Percent of lines that are numbers: 100.00
      Non-numeric strings in file (with count): NONE
    
    
    Sum: 6.07
    Number of Numbers: 3
    
    
    Strings Present?
      "":  false
      " ":  false
    
    
    Done!



Testing: Exercise 2, The File Scanner
-------------------------------------
To execute a non-JUnit test, you can use the included test driver.  Run:
`java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver <pathToScan>`

### Manual Test Executions
Included are some example test executions, both the command and expected
output.  You can run these same commands from the top-level directory of
the program distribution and should see the same results.  Note that 
this was tested on Ubuntu 14.04 LTS from bash.

The test resources are located in `src/test/resources/scanning`.  The directory
is laid out as follows
  dirA/
    dir1/
      emptyFile.txt -- a 0B file
      lorem2055 -- a 2055B text file with ipsum lorem
    dir2/
      lorem71919 -- a 71919B text file with ipsum lorem
  dirB/
    LICENSE -- a 313B file containing the image license info
    Minneapolis-20060311.jpg -- a 9245B file containing an image of Minneapolis
  dirC/ -- only created for Test 6
  dirD/ -- an empty directory
     

#### Test 1: Invalid File
This checks that an exception is thrown if we attempt to scan a 
non-existent path (bad input).

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver badFileName
    Error encountered running the scanner.  Error details below:
    
    java.nio.file.NoSuchFileException: badFileName
        at sun.nio.fs.UnixException.translateToIOException(UnixException.java:86)
        at sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:102)
        at sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:107)
        at sun.nio.fs.UnixFileAttributeViews$Basic.readAttributes(UnixFileAttributeViews.java:55)
        at sun.nio.fs.UnixFileSystemProvider.readAttributes(UnixFileSystemProvider.java:144)
        at sun.nio.fs.LinuxFileSystemProvider.readAttributes(LinuxFileSystemProvider.java:99)
        at java.nio.file.Files.readAttributes(Files.java:1737)
        at java.nio.file.FileTreeWalker.getAttributes(FileTreeWalker.java:219)
        at java.nio.file.FileTreeWalker.visit(FileTreeWalker.java:276)
        at java.nio.file.FileTreeWalker.walk(FileTreeWalker.java:322)
        at java.nio.file.FileTreeIterator.<init>(FileTreeIterator.java:72)
        at java.nio.file.Files.walk(Files.java:3574)
        at java.nio.file.Files.walk(Files.java:3625)
        at com.code42.scanning.FileScanner.scan(FileScanner.java:43)
        at com.code42.scanning.FileScannerDriver.main(FileScannerDriver.java:43)
    
    
    Terminating test driver...


#### Test 2: Valid File
Checks that we can scan a single file (without looking at the directory).

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirA/dir1/lorem2055 
    Number of files scanned: 1
    Number of directories scanned: 0
    Total size (in B) of all scanned files: 2055
    Average size (in B) of all scanned files: 2055


#### Test 3: Valid Directory with no Subdirectories
Checks that we can scan a valid directory that contains no 
subdirectories.  The valid directory is non-empty and contains text 
files.

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirA/dir1
    Number of files scanned: 2
    Number of directories scanned: 1
    Total size (in B) of all scanned files: 2055
    Average size (in B) of all scanned files: 1027


#### Test 4: Valid directory with Multiple, Non-empty Subdirectories
Checks that we can scan a directory that contains a number of non-empty 
subdirectories (each containing only text files).

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirA
    Number of files scanned: 3
    Number of directories scanned: 3
    Total size (in B) of all scanned files: 73974
    Average size (in B) of all scanned files: 24658


#### Test 5: Valid directory with a Non-text File
Checks that we can scan both binary and text files.

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirB
    Number of files scanned: 2
    Number of directories scanned: 1
    Total size (in B) of all scanned files: 9558
    Average size (in B) of all scanned files: 4779


#### Test 6: Symlinks
Checks that symlinks are not followed and that we do not have a divide 
by 0 error when computing the average.

* Preparation
    cd src/test/resources/scanning/
    mkdir dirC
    cd dirC
    ln -s $(cd "../dirA"; echo $(pwd)) dirA
    ln -s $(cd "../dirB"; echo $(pwd)) dirB
    
* Execution
    >$java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirC
    Number of files scanned: 0
    Number of directories scanned: 3
    Total size (in B) of all scanned files: 0
    Average size (in B) of all scanned files: -1

Notice that the result was a negative average size.  At this point, that
means that the size is not computable.  I use -1 because I didn't want 
to change the given signature to allow me to properly throw an exception
here.  Further, I wasn't sure if throwing a RuntimeException is 
permitted by the organization's coding standards.


#### Test 7: Empty Directory
Checks that we do not have a divide by 0 error when computing the 
average.  See note in Test 6 regarding a -1 average.

    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirD
    Number of files scanned: 0
    Number of directories scanned: 1
    Total size (in B) of all scanned files: 0
    Average size (in B) of all scanned files: -1
