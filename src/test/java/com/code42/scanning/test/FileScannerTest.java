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

import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Created by ian on 5/11/16.
 */
public class FileScannerTest
{
    @Test
    public void testScan() throws IOException
    {
        FileScanner.ScanResult result = FileScanner.scan("src/test/resources/scanning/dirA");
        Assert.assertEquals(2, result.getNumFiles());
        Assert.assertEquals(2055, result.getTotalBytes());
        Assert.assertEquals(2, result.getNumDirectories());
        Assert.assertEquals(1027, result.getAvgBytes());
    }


    @Test
    public void testScanOfNonEmptyFile() throws IOException
    {
        FileScanner.ScanResult result = FileScanner.scan("src/test/resources/scanning/dirA/dir1/lorem2055");
        Assert.assertEquals(1, result.getNumFiles());
        Assert.assertEquals(2055, result.getTotalBytes());
        Assert.assertEquals(0, result.getNumDirectories());
        Assert.assertEquals(2055, result.getAvgBytes());
    }


    @Test
    public void testScanOfEmptyFile() throws IOException
    {
        FileScanner.ScanResult result = FileScanner.scan("src/test/resources/scanning/dirA/dir1/emptyFile.txt");
        Assert.assertEquals(1, result.getNumFiles());
        Assert.assertEquals(0, result.getTotalBytes());
        Assert.assertEquals(0, result.getNumDirectories());
        Assert.assertEquals(0, result.getAvgBytes());
    }


    @Test(expected = NoSuchFileException.class)
    public void testScanOfInvalid() throws IOException
    {
        FileScanner.scan("src/test/resources/scanning/dirA/invalid");
    }
}
