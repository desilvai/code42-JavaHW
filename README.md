code42-JavaHW
=============
Code42 Interview Code


Build Instructions
------------------
* To build using the enclosed gradle wrapper, execute the following:
  `./gradlew build`
  This was tested using gradle version


Testing: Exercise 1, The Input Analyzer
---------------------------------------


Testing: Exercise 2, The File Scanner
-------------------------------------
To execute a non-JUnit test, you can use the included test driver.  Run:
`java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver <pathToScan>`

### Example Test Executions
Included are some example test executions, both the command and expected output.  You can run these same commands from the top-level directory of the program distribution and should see the same results.  Note that this was tested on Ubuntu 14.04 LTS from bash.

#### Test 1: Invalid File
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
    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirA/dir1/lorem2055 
    Number of files scanned: 1
    Number of directories scanned: 0
    Total size (in B) of all scanned files: 2055
    Average size (in B) of all scanned files: 2055


#### Test 3: Valid Directory with no Subdirectories
    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirA/dir1
    Number of files scanned: 2
    Number of directories scanned: 1
    Total size (in B) of all scanned files: 2055
    Average size (in B) of all scanned files: 1027


#### Test 4: Valid directory with Multiple, Non-empty Subdirectories
    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirA
    Number of files scanned: 3
    Number of directories scanned: 3
    Total size (in B) of all scanned files: 73974
    Average size (in B) of all scanned files: 24658


#### Test 5: Valid directory with a Non-text File
    >$ java -classpath build/libs/code42-1.0.jar  com.code42.scanning.FileScannerDriver src/test/resources/scanning/dirB
    Number of files scanned: 2
    Number of directories scanned: 1
    Total size (in B) of all scanned files: 9558
    Average size (in B) of all scanned files: 4779


#### Test 6: Symlinks
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