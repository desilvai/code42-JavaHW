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

/**
 * Created by ian on 5/11/16.
 */
public class FileScannerTest
{
    @Test
    public void testScan() throws IOException
    {
        FileScanner.ScanResult result = FileScanner.scan("/home/ian/git/code42-JavaHW/src/test/resources");
        Assert.assertEquals(2, result.getNumFiles());
        Assert.assertEquals(61, result.getTotalBytes());
        Assert.assertEquals(2, result.getNumFiles());
        Assert.assertEquals(2, result.getNumDirectories());
        Assert.assertEquals(30, result.getAvgBytes());
    }
}
