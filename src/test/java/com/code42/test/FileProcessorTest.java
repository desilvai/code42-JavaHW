/*
 * Copyright (c) 2016, Ian J. De Silva
 * All Rights Reserved
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */

package com.code42.test;

import com.code42.FileProcessor;
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
    public void testWithGivenSampleInput() throws IOException
    {
        String testFileName = "givenSampleInput.txt";
        String expectedOutput = "Analysis of file, " + testFileName + "\n" +
                "  Sum of Numbers: 16.20\n" +
                "  Average of Numbers: 5.40\n" +
                "  Median of Numbers: 5.00\n" +
                "  Percent of lines that are numbers: 42.86\n";

        // Given
        FileProcessor processor = new FileProcessor();

        // When
        File inputFile = new File(TEST_RESOURCES_PATH +
                                          File.separator +
                                          testFileName);
        processor.readFile(inputFile);

        // Then
        Assert.assertEquals(expectedOutput, processor.toString());
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

        // Given
        FileProcessor processor = new FileProcessor();

        // When
        File inputFile = new File(TEST_RESOURCES_PATH +
                                          File.separator +
                                          testFileName);
        processor.readFile(inputFile);

        // Then -- we never get here.
    }
}
