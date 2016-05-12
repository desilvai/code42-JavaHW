/*
 * Copyright (c) 2016, Ian J. De Silva
 * All Rights Reserved
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */

package com.code42.scanning.test;

import com.code42.scanning.FileScanner;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Created by ian on 5/11/16.
 */
public class FileScannerTest
{
    /**
     * The common directory where all of the tests are stored.
     */
    private static final String TEST_FILE_DIRECTORY = "src" + File.separator +
                                                      "test" + File.separator +
                                                      "resources" + File.separator +
                                                      "scanning";


    /**
     * Attempts to scan an invalid path.
     *
     * @throws IOException  We expect that a NoSuchFileException is thrown.
     */
    @Test(expected = NoSuchFileException.class)
    public void testScanOfInvalid() throws IOException
    {
        // Filename: "src/test/resources/scanning/dirA/invalid"
        String fileName = TEST_FILE_DIRECTORY + File.separator +
                "dirA" + File.separator + "invalid";
        FileScanner.scan(fileName);
    }

    /**
     * Attempts to scan an invalid path.
     *
     * @throws IOException  We expect that a NoSuchFileException is thrown.
     */
    @Test(expected = NoSuchFileException.class)
    public void testScanOfNullFile() throws IOException
    {
        String fileName = null;
        FileScanner.scan(fileName);
    }


    /**
     * Scans a single non-empty file to ensure it can handle non-directory
     * paths.
     *
     * @throws IOException  shouldn't happen.
     */
    @Test
    public void testScanOfNonEmptyFile() throws IOException
    {
        // Filename: "src/test/resources/scanning/dirA/dir1/lorem2055"
        String fileName = TEST_FILE_DIRECTORY + File.separator +
                "dirA" + File.separator +
                "dir1" + File.separator +
                "lorem2055";
        FileScanner.ScanResult result = FileScanner.scan(fileName);

        Assert.assertEquals(1, result.getNumFiles());
        Assert.assertEquals(0, result.getNumDirectories());
        Assert.assertEquals(2055, result.getTotalBytes());
        Assert.assertEquals(2055, result.getAvgBytes());
    }


    /**
     * Scans a single empty file to ensure it can handle files of size 0.
     *
     * @throws IOException  shouldn't happen.
     */
    @Test
    public void testScanOfEmptyFile() throws IOException
    {
        // Filename: "src/test/resources/scanning/dirA/dir1/emptyFile.txt"
        String fileName = TEST_FILE_DIRECTORY + File.separator +
                "dirA" + File.separator +
                "dir1" + File.separator +
                "emptyFile.txt";
        FileScanner.ScanResult result = FileScanner.scan(fileName);

        Assert.assertEquals(1, result.getNumFiles());
        Assert.assertEquals(0, result.getNumDirectories());
        Assert.assertEquals(0, result.getTotalBytes());
        Assert.assertEquals(0, result.getAvgBytes());
    }


    /**
     * Scans an directory with no subdirectories to check the directory count.
     *
     * @throws IOException  shouldn't happen.
     */
    @Test
    public void testScanDirectoryWithNoSubdirectories() throws IOException
    {
        // Filename: "src/test/resources/scanning/dirA/dir1"
        String fileName = TEST_FILE_DIRECTORY + File.separator +
                "dirA" + File.separator +
                "dir1";
        FileScanner.ScanResult result = FileScanner.scan(fileName);

        Assert.assertEquals(2, result.getNumFiles());
        Assert.assertEquals(1, result.getNumDirectories());
        Assert.assertEquals(2055, result.getTotalBytes());
        Assert.assertEquals(1027, result.getAvgBytes());
    }


    /**
     * Scans an directory with multiple subdirectories to ensure that the
     * subdirectories are correctly counted.
     *
     * @throws IOException  shouldn't happen.
     */
    @Test
    public void testScanOfMultipleDirectories() throws IOException
    {
        // Filename: "src/test/resources/scanning/dirA"
        String fileName = TEST_FILE_DIRECTORY + File.separator + "dirA";
        FileScanner.ScanResult result = FileScanner.scan(fileName);

        Assert.assertEquals(3, result.getNumFiles());
        Assert.assertEquals(3, result.getNumDirectories());
        Assert.assertEquals(73974, result.getTotalBytes());
        Assert.assertEquals(24658, result.getAvgBytes());
    }


    /**
     * Scans an directory with multiple types of files (text and binary data)
     * to ensure that multiple file types will not cause problems with the
     * scanner.
     *
     * @throws IOException  shouldn't happen.
     */
    @Test
    public void testScanDirectoryWithMultipleFileTypes() throws IOException
    {
        // Filename: "src/test/resources/scanning/dirB"
        String fileName = TEST_FILE_DIRECTORY + File.separator +
                          "dirB";
        FileScanner.ScanResult result = FileScanner.scan(fileName);

        Assert.assertEquals(2, result.getNumFiles());
        Assert.assertEquals(1, result.getNumDirectories());
        Assert.assertEquals(9558, result.getTotalBytes());
        Assert.assertEquals(4779, result.getAvgBytes());
    }


    /**
     * Scans an empty directory to ensure the average computation doesn't
     * attempt to divide by 0.
     *
     * @throws IOException  shouldn't happen.
     */
    @Test
    public void testScanEmptyDirectory() throws IOException
    {
        // Filename: "src/test/resources/scanning/dirD"
        String fileName = TEST_FILE_DIRECTORY + File.separator +
                "dirD";

        FileScanner.ScanResult result = FileScanner.scan(fileName);
        Assert.assertEquals(0, result.getNumFiles());
        Assert.assertEquals(1, result.getNumDirectories());
        Assert.assertEquals(0, result.getTotalBytes());
        Assert.assertEquals(-1, result.getAvgBytes());
    }
}
