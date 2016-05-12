/*
 * Copyright (c) 2016, Ian J. De Silva
 * All Rights Reserved
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */

package com.code42.inputAnalysis;

import java.io.File;
import java.io.IOException;

/**
 * Created by ian on 5/12/16.
 */
public class InputAnalysisDriver
{
    /**
     * The main for the test driver that can be used to verify the
     * FileProcessor.
     * @param args  the command-line arguments.  This application takes
     *              a few.  The first argument is the path to the file
     *              that should be analyzed.  All other arguments are
     *              strings that we will check if they are present in the
     *              parsed file.
     */
    public static void main(String args[])
    {
        // Check the command-line arguments.
        if(args.length == 0)
        {
            System.err.println("Invalid number of arguments (" + args.length +
                                       ").\n");
            System.err.println("Usage:");
            System.err.println("programName <path> [stringToFind1] [stringToFind2] ...");
            return;
        }

        // Get the path
        String path = args[0];
        if(path == null)
        {
            System.err.println("ERROR: Path cannot be null!");
        }

        // Open and parse the file
        FileProcessor processor;
        try
        {
            File inputFile = new File(path);
            processor = new FileProcessor(inputFile);
        }
        catch(IOException e)
        {
            // If there was an issue, throw an error.
            System.err.println("Error encountered running the analysis.  " +
                                       "Error details below:\n");
            e.printStackTrace();

            System.err.println("\n\nTerminating test driver...\n");
            return;
        }

        // Get the stats on the file
        System.out.println("Printed String:");
        processor.printFileStatistics();

        // Print the other, accessible stats and ensure that they are rounded
        // correctly.
        System.out.println("\n\nSum: " + processor.getTotal());
        System.out.println("Number of Numbers: " +
                                   processor.getCountOfNumbers());

        // Check if the strings are present.
        if(args.length == 1)
        {
            System.out.println("\n\nDone!");
            return;
        }
        //else
        System.out.println("\n\nStrings Present?");

        // Display the name of the string and true if the string is present
        // in the file, or false otherwise.
        for(int k = 1; k < args.length; k++)
        {
            System.out.println("  \"" + args[k] + "\":  " +
                                       processor.contains(args[k]));
        }

        System.out.println("\n\nDone!");
    }
}
