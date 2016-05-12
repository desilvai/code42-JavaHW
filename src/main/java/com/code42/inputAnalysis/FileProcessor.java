/*
 * Copyright (c) 2016, Ian J. De Silva
 * All Rights Reserved
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */
package com.code42.inputAnalysis;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * This class defines an immutable object that parses a specified file and
 * stores the information it memory.  This object allows the caller to
 * interrogate the parser about the contents of the file.
 */
public final class FileProcessor
{
    //--------------------------------------------------------------------
    //  CONSTANTS
    //--------------------------------------------------------------------
    /**
     * The precision of the decimals to use when printing numbers.
     */
    private static final int DECIMAL_PRECISION = 2;

    /**
     * We will always round up if the digit to the right of the precision
     * digit is 5 or more.  We will round down otherwise.
     */
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;


    //--------------------------------------------------------------------
    //  DATA MEMBERS
    //--------------------------------------------------------------------
    /**
     * The name of the file.
     */
    private String fileName = "";

    /**
     * The line count of the file
     */
    private int lineCount = 0;

    /**
     * An ArrayList has an amortized insertion time of O(1) (the same as a
     * linked list).  Java's sort algorithm takes O(n lg n) to sort it.  We
     * can then find the median in O(1) time.  Using two heaps, we can get
     * this to O(lg n) insertion (O(n lg n) total insertions) and
     * O(1) to find the median.  Using Java's built-ins are more maintainable,
     * so we choose those.
     *
     * It is possible that there are numbers in the input file that are very
     * large, so we chose to use BigDecimals to store them.  Yes, this is
     * slower than using a Double, but it gives us some added robustness and
     * it keeps the types consistent within the file.  BigDecimals also
     * have better rounding support, so there is some added benefit there.
     */
    private List<BigDecimal> numbers = new ArrayList<>();

    // TODO -- Verify this restriction on the contents of the input file.
    /**
     * It is plausible that the sum of all numbers within the input file are
     * greater than Double.MAX_VALUE, so we use BigDecimal to give us some
     * protection.  Also, BigDecimal provides us with a nice way of rounding
     * values.
     */
    private BigDecimal sum = BigDecimal.valueOf(0);

    /**
     * Need to store the strings and the number of occurrences in the file.
     * With the right hashing function and a sufficiently large table, this
     * has O(1) insertion/lookups.
     */
    private Map<String, Integer> nonNumericStrings = new HashMap<>();



    //--------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------
    /**
     * Constructor
     *
     * Reads in a given text file and parses it, looking for numbers and
     * non-numeric strings.  For numbers,
     * @param file  the file to read in and parse
     * @throws NoSuchFileException  if the file name is null
     * @throws java.io.FileNotFoundException  if there is no file on the
     *              filesystem with the given name.
     * @throws IOException  if there was some other error occurred when
     *              opening/reading the specified file.
     */
    public FileProcessor(File file) throws IOException
    {
        if(null == file)
        {
            // No file to process.  Throw an exception.
            throw new NoSuchFileException("The file name cannot be null.");
        }

        // Using a BufferedReader here handles multiple newline formats.
        try( BufferedReader reader = new BufferedReader(new FileReader(file)) )
        {
            fileName = file.getName();

            reader.lines().forEach(this::processLine);

            // Sort the list of numbers now so we don't have to do it every
            // time we want the median.
            Collections.sort(numbers);
        }
    }

    //--------------------------------------------------------------------
    //  Public API
    //--------------------------------------------------------------------
    /**
     * Gets the sum of all the numbers that appeared in the file.
     *
     * @return the sum of all the numbers in the file.  If the sum is too
     *         large/small to be represented as a double, it will be returned
     *         as +/- infinity respectively.
     */
    public double getTotal()
    {
        return sum.setScale(DECIMAL_PRECISION,
                            ROUNDING_MODE)
                  .doubleValue();
    }


    /**
     * Gets the quantity of lines in the file that contained numbers
     *
     * @return  the count of how many numbers were read from the source file
     */
    public int getCountOfNumbers()
    {
        return numbers.size();
    }


    // TODO -- verify requirement: Do we also look for numbers?  I'm assuming
    // no.
    /**
     * Checks if the file contained the non-numeric string.
     *
     * @param src  the string to find in the list of strings for the file.
     *
     * @return  true if the file contains the provided string.  This will
     *          return false if src is parsable as a number or if it contains
     *          newline characters.
     */
    public boolean contains(String src)
    {
        if(null == src)
        {
            return false;
        }
        return nonNumericStrings.containsKey(src);
    }


    /**
     * Prints out the file's statistics to standard out as specified by
     * {@link #toString()}.
     */
    public void printFileStatistics()
    {
        System.out.print(toString());
    }


    /**
     * Formats the statistics of the file as a string.  This contains the
     * following:
     * <ul>
     *     <li>the name of the file</li>
     *     <li>the sum of the numbers</li>
     *     <li>the average of the numbers (if any numbers are present)</li>
     *     <li>the median of the numbers (if any numbers are present)</li>
     *     <li>the percent of lines in the file containing numbers</li>
     *     <li>a reverse-alphabetical, distinct list of strings found in the
     *          file with number of times that string appeared</li>
     * </ul>
     *
     * @return  a formatted (pretty-print) string containing the statistics of
     *          the file
     */
    public String toString()
    {
        StringBuilder outputString = new StringBuilder();
        Formatter formatter = new Formatter(outputString);

//        // TODO -- Verify that this is acceptable.
//        // Output the filename so we have an easier time debugging.
//        String fileName = this.fileName;
//        if(fileName == null || fileName.isEmpty())
//        {
//            fileName = "NO FILE SPECIFIED";
//        }
//
//        formatter.format("Analysis of file, %s\n", fileName);

        // TODO -- Check the formatting of the output.  Is pretty printing ok?
        // Sum
        final String labelFormat =  "  %s: %." + DECIMAL_PRECISION + "f\n";
        formatter.format(labelFormat, "Sum of Numbers", sum);

        // If there are no numbers, we can't find the average or median.
        if(numbers.size() == 0)
        {
            // TODO -- What should be emitted if there are no numbers?
            formatter.format("  Average of Numbers: UNDEFINED\n");
            formatter.format("  Median of Numbers: NONE\n");
        }
        else
        {
            // These will be non-null.
            // Average
            formatter.format(labelFormat,
                             "Average of Numbers",
                             getArithmeticMean());

            // Median
            formatter.format(labelFormat,
                             "Median of Numbers",
                             getMedian());
        }

        // Percentages
        if(lineCount > 0)
        {
            double percentNumbers = (numbers.size() * 100.0) / lineCount;
            formatter.format(labelFormat,
                             "Percent of lines that are numbers",
                             percentNumbers);
        }
        else
        {
            // TODO -- What should be emitted if there are no lines in the file.
            formatter.format("  Percent of lines that are numbers: UNDEFINED (no lines parsed)\n");
        }

        // Print strings in reverse order (S

        formatter.format("  Non-numeric strings in file (with count):");
        String[] keys = nonNumericStrings.keySet().toArray(new String[0]);
        if(keys.length == 0)
        {
            formatter.format(" NONE\n");
        }
        else
        {
            formatter.format("\n");

            // StackOverflow had a nicer way of reversing the string array
            // (using an existing comparator), so I'm using that:
            // http://stackoverflow.com/questions/13779643/sorting-an-array-of-strings-in-reverse-alphabetical-order-in-java#13780089)
            // Performance: O(n lg n) to sort the keys, O(n) to print them.
            Arrays.sort(keys,
                        Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));

            for(String key : keys)
            {
                Integer count = nonNumericStrings.get(key);
                formatter.format("    %s:%d\n", key, count);
            }
        }


        return outputString.toString();
    }


    //--------------------------------------------------------------------
    //  Helper Methods
    //--------------------------------------------------------------------
    /**
     * Processes a single line in the file.  If it is parsable into a number,
     * the number is added to the numbers list.  Otherwise, it is added to the
     * strings list.
     *
     * @param line  the line in the file (without the newline characters) to
     *              process
     */
    private void processLine(String line)
    {
        lineCount++;

        // if line is a number, add it to the numbers list.
        // What's a number?  We will assume it is any format that is
        // parsable by BigDecimal (so a base 10 representation of a string; see
        // the JavaDoc for more info.).
        try
        {
            BigDecimal numberValue = new BigDecimal(line);

            numbers.add(numberValue);
            sum = sum.add(numberValue);
        }
        catch(NumberFormatException e)
        {
            // else line is a string.

            // If the line is present in the mapping, add 1 to the count.
            // Otherwise, add it with a count (value) of 1.
            nonNumericStrings.merge(line, 1, Integer::sum);
        }
    }


    /**
     * Gets the arithmetic mean of all the numbers that appeared in the file.
     *
     * @return  the arithmetic mean of the numbers in the file or null if
     *          there are no numbers in the file
     */
    private Number getArithmeticMean()
    {
        // Make sure we don't divide by 0!
        if(numbers.isEmpty())
        {
            return null;
        }

        BigDecimal quantity = BigDecimal.valueOf(numbers.size());
        return sum.divide(quantity,
                          DECIMAL_PRECISION,
                          ROUNDING_MODE);
    }


    /**
     * Gets the median of all numbers that appeared in the file.
     *
     * @return  the median value of all numbers in the file or null if there
     *          are no numbers in the file.
     */
    private Number getMedian()
    {
        // If there are no numbers, the median is undefined.
        if(numbers.isEmpty())
        {
            return null;
        }

        // Median
        BigDecimal median;
        int medianPosition = numbers.size() / 2;

        if (numbers.size() % 2 != 0)
        {
            // There is an odd number of elements.  The median is at
            // position size / 2 (integer division).  There is no need
            // to add 1 to this since the array is 0-indexed.
            median = numbers.get(medianPosition);

            // Round the median
            median = median.setScale(DECIMAL_PRECISION,
                                     ROUNDING_MODE);
        }
        else  // There is an even number of elements
        {
            // TODO -- Verify that we want to use the average of the two
            // possible median values.
            BigDecimal medianHigh = numbers.get(medianPosition);

            // This is present because size must be at least 2
            // (because the size is non-zero and even).
            BigDecimal medianLow = numbers.get(medianPosition - 1);

            median = medianHigh.add(medianLow)
                               .divide(BigDecimal.valueOf(2),
                                       DECIMAL_PRECISION,
                                       ROUNDING_MODE);
        }

        return median;
    }
}
