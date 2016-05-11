/**
 * Copyright (c) 2016, Ian J. De Silva
 * All rights reserved.
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */
package com.code42;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by ian on 5/4/16.
 */
public class FileProcessor
{
    //--------------------------------------------------------------------
    //  CONSTANTS
    //--------------------------------------------------------------------
    /**
     * The precision of the decimals to use when printing numbers.
     */
    private static final int DECIMAL_PRECISION = 2;
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
     * Also, we assume that there is no number in the input file that is
     * greater than Double.MAX_VALUE (we will further restrict this for
     * summation).
     */
    private List<BigDecimal> numbers = new ArrayList<>();

    // TODO -- Verify this restriction on the contents of the input file.
    /**
     * While it is certainly plausible that the sum of all numbers within the
     * input file are greater than Double.MAX_VALUE, we will assume that this
     * will not occur.
     */
    private BigDecimal sum = BigDecimal.valueOf(0);

    /**
     * Need to store the strings and the number of occurrences in the file.
     * With the right hashing function and a sufficiently large table, this
     * has O(1) insertion/lookups.
     */
    private Map<String, Integer> nonNumericStrings = new HashMap<>();



    //--------------------------------------------------------------------
    //  Public API
    //--------------------------------------------------------------------
    /**
     * Reads in a given text file and parses it, looking for numbers and
     * non-numeric strings.  For numbers,
     * @param file  the file to read in and parse
     */
    public void readFile(File file) throws IOException
    {
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
     *          return false if src is a number.
     */
    public boolean contains(String src)
    {
        // TODO
        throw new NotImplementedException();
    }


    /**
     * Prints out the file's statistics to standard out.
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

        if(numbers.size() == 0)
        {
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
        double percentNumbers = (numbers.size() * 100.0) / lineCount;
        formatter.format(labelFormat,
                         "Percent of lines that are numbers",
                         percentNumbers);

        // Print strings in reverse order (StackOverflow had a nicer way
        // of reversing the string array, so I'm using that:
        // http://stackoverflow.com/questions/13779643/sorting-an-array-of-strings-in-reverse-alphabetical-order-in-java#13780089)
        // Performance: O(n lg n) to sort the keys, O(n) to print them.
        String[] keys = nonNumericStrings.keySet().toArray(new String[0]);
        Arrays.sort(keys,
                    Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));

        formatter.format("  Non-numeric strings in file:\n");
        for(String key : keys)
        {
            Integer count = nonNumericStrings.get(key);
            formatter.format("    %s:%d\n", key, count);
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
        return sum.divide(quantity).setScale(DECIMAL_PRECISION,
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
        }
        else  // There is an even number of elements
        {
            // TODO -- Verify that we want to use the average of the two
            // possible median values.
            BigDecimal medianHigh = numbers.get(medianPosition);

            // This is present because size must be at least 2
            // (because the size is non-zero and even).
            BigDecimal medianLow = numbers.get(medianPosition - 1);

            median = medianHigh.add(medianLow).divide(BigDecimal.valueOf(2));
        }

        return median.setScale(DECIMAL_PRECISION,
                               ROUNDING_MODE);
    }


    //--------------------------------------------------------------------
    //  Main
    //--------------------------------------------------------------------
    public static void main(String args[])
    {
    }
}
