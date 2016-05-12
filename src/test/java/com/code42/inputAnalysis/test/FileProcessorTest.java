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
import java.nio.file.NoSuchFileException;

/**
 * The automated tests for the {@link FileProcessor}.  This focuses primarily
 * on {@link FileProcessor#toString()} since the requirement was to have
 * {@link FileProcessor} emit directly to standard out.
 *
 * Created by ian on 5/10/16.
 */
public class FileProcessorTest
{
    /**
     * The root of the test resources.
     */
    private static final String TEST_RESOURCES_PATH = "src" + File.separator +
                                                      "test" + File.separator +
                                                      "resources" + File.separator +
                                                      "fileProcessor";

    /**
     * The permitted tolerance for differences between actual and expected
     * doubles.
     */
    private static final double ALLOWED_DELTA = 1e-15;



    /**
     * Run a test using the provided sample input.
     *
     * @throws IOException  fail if this is thrown
     */
    @Test
    public void testWithGivenSampleInput() throws IOException
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

        // Check the sum.
        double sum = processor.getTotal();
        Assert.assertEquals(16.2, sum, ALLOWED_DELTA);
    }


    /**
     * Tests the contains method using the given sample input file.
     * @throws IOException  shouldn't happen
     */
    @Test
    public void testContainsWithGivenSampleInput() throws IOException
    {
        String testFileName = "givenSampleInput.txt";

        // Given
        File inputFile = new File(TEST_RESOURCES_PATH +
                                          File.separator +
                                          testFileName);
        FileProcessor processor = new FileProcessor(inputFile);

        // Check the strings -- ideally, these should be in their own tests
        Assert.assertEquals(false,
                            processor.contains(""));

        Assert.assertEquals(false,
                            processor.contains("jumped over the lazy dog"));

        Assert.assertEquals(true,
                            processor.contains("jumped over the lazy dog."));
    }


    /**
     * Tests the contains method returns false for numbers.
     * @throws IOException  shouldn't happen
     */
    @Test
    public void testContainsForNumbersWithGivenSampleInput() throws IOException
    {
        String testFileName = "givenSampleInput.txt";

        // Given
        File inputFile = new File(TEST_RESOURCES_PATH +
                                          File.separator +
                                          testFileName);
        FileProcessor processor = new FileProcessor(inputFile);

        // Check the strings -- ideally, these should be in their own tests
        Assert.assertEquals(false, processor.contains("0"));
        Assert.assertEquals(false, processor.contains("5"));
        Assert.assertEquals(false, processor.contains("4.2"));
        Assert.assertEquals(false, processor.contains("7"));
    }


    /**
     * Tests the contains method does not match null when using the given
     * sample input file.
     * @throws IOException  shouldn't happen
     */
    @Test
    public void testContainsForNullWithGivenSampleInput() throws IOException
    {
        String testFileName = "givenSampleInput.txt";

        // Given
        File inputFile = new File(TEST_RESOURCES_PATH +
                                          File.separator +
                                          testFileName);
        FileProcessor processor = new FileProcessor(inputFile);

        // Check the strings -- ideally, these would be in their own tests
        Assert.assertEquals(false, processor.contains(null));
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
     * Try to parse a null file.  Make sure we get a
     * NoSuchFileException.
     *
     * @throws IOException  we expect a NoSuchFileException
     */
    @Test(expected = NoSuchFileException.class)
    public void testWithNullFile() throws IOException
    {
        // Parse the file
        // There isn't a file to parse, so this will succeed silently.
        FileProcessor processor = new FileProcessor(null);

        // Then -- we never get here.
    }


    /**
     * Run a test using an empty file.
     *
     * @throws IOException  fail if this is thrown
     */
    @Test
    public void testWithEmptyFile() throws IOException
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

        // Check the sum.
        double sum = processor.getTotal();
        Assert.assertEquals(0, sum, ALLOWED_DELTA);

        // Check for an empty string
        Assert.assertEquals(false, processor.contains(""));
    }
}
