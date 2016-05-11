/**
 * Copyright (c) 2016, Ian J. De Silva
 * All rights reserved.
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */
package com.code42;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

/**
 * Created by ian on 5/4/16.
 */
public class FileProcessor
{
    private static final int DECIMAL_PRECISION = 2;

    private String fileName = "";
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
     * Reads in a given text file and parses it, looking for numbers and
     * non-numeric strings.  For numbers,
     * @param file
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

    public String toString()
    {
        StringBuilder outputString = new StringBuilder();
        Formatter formatter = new Formatter(outputString);

        // TODO -- Verify that this is acceptable.
        // Output the filename so we have an easier time debugging.
        String fileName = this.fileName;
        if(fileName == null || fileName.isEmpty())
        {
            fileName = "NO FILE SPECIFIED";
        }

        formatter.format("Analysis of file, %s\n", fileName);

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
            // Average
            BigDecimal average = sum.divide(BigDecimal.valueOf(numbers.size()));
            formatter.format(labelFormat, "Average of Numbers", average);

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
            formatter.format(labelFormat, "Median of Numbers", median);

            // Percentages
            double percentNumbers = (numbers.size() * 100.0) / lineCount;
            formatter.format(labelFormat, "Percent of lines that are numbers", percentNumbers);
        }

        return outputString.toString();
    }


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
        }
    }

    public static void main(String args[])
    {
    }
}
