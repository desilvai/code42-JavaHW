/*
 * Copyright (c) 2016, Ian J. De Silva
 * All Rights Reserved
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */

package com.code42.inputAnalysis.test;

import com.code42.inputAnalysis.FileProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ian on 5/10/16.
 */
public class FileProcessorTest
{
    private static final String TEST_RESOURCES_PATH = "src" + File.separator +
                                                      "test" + File.separator +
                                                      "resources" + File.separator +
                                                      "fileProcessor";

    /**
     * Run a test using the provided sample input.
     *
     * @throws IOException  fail if this is thrown
     */
    @Test
    public void testToStringWithGivenSampleInput() throws IOException
    {
        String testFileName = "givenSampleInput.txt";
        String expectedOutput = "  Sum of Numbers: 16.20\n" +
                "  Average of Numbers: 5.40\n" +
                "  Median of Numbers: 5.00\n" +
                "  Percent of lines that are numbers: 42.86\n" +
                "  Non-numeric strings in file (with count):\n" +
                "    The quick brown fox:1\n" +
                "    jumped over the lazy dog.:1\n" +
                "    foo:2\n";

        // Given
        File inputFile = new File(TEST_RESOURCES_PATH +
                                          File.separator +
                                          testFileName);
        FileProcessor processor = new FileProcessor(inputFile);

        // Get the stats on the file
        String actualOutput = processor.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }


    /**
     * Try to parse a non-existent file.  Make sure we get a
     * FileNotFoundException.
     *
     * @throws IOException  we expect a FileNotFoundException
     */
    @Test(expected = FileNotFoundException.class)
    public void testWithNonExistentFile() throws IOException
    {
        String testFileName = "nonexistentFile";

        // Parse the file
        File inputFile = new File(TEST_RESOURCES_PATH +
                                          File.separator +
                                          testFileName);
        FileProcessor processor = new FileProcessor(inputFile);

        // Then -- we never get here.
    }


    /**
     * Run a test using an empty file.
     *
     * @throws IOException  fail if this is thrown
     */
    @Test
    public void testToStringWithEmptyFile() throws IOException
    {
        String testFileName = "emptySampleInput.txt";
        String expectedOutput = "  Sum of Numbers: 0.00\n" +
                "  Average of Numbers: UNDEFINED\n" +
                "  Median of Numbers: NONE\n" +
                "  Percent of lines that are numbers: UNDEFINED (no lines parsed)\n" +
                "  Non-numeric strings in file (with count): NONE\n";

        // Parse the file
        File inputFile = new File(TEST_RESOURCES_PATH +
                                          File.separator +
                                          testFileName);
        FileProcessor processor = new FileProcessor(inputFile);

        // Get the stats on the file
        String actualOutput = processor.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}
