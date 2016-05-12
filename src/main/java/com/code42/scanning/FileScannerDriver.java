/*
 * Copyright (c) 2016, Ian J. De Silva
 * All Rights Reserved
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */

package com.code42.scanning;

import java.io.IOException;
import java.util.Formatter;

/**
 * Created by ian on 5/11/16.
 */
public class FileScannerDriver
{
    /**
     * A test driver for the FileScanner
     * @param args  the command line arguments.  This expects one argument,
     *              the path to the directory/file to be scanned.  This path
     *              may be absolute or relative (do not include special
     *              symbols like "..").
     */
    public static void main(String[] args)
    {
        // Check the command-line arguments.
        if(args.length != 1)
        {
            System.err.println("Invalid number of arguments (" + args.length +
                               ").  Expected only the path as an argument.");
            return;
        }

        String path = args[0];

        FileScanner.ScanResult results;
        try
        {
            // Run the scanner.
            results = FileScanner.scan(path);
        }
        catch(IOException e)
        {
            System.err.println("Error encountered running the scanner.  " +
                               "Error details below:\n");
            e.printStackTrace();

            System.err.println("\n\nTerminating test driver...\n");
            return;
        }

        // Print out the results.
        // results will never be null here, but since this is a test driver,
        // check it anyway.
        if(results == null)
        {
            System.err.println("Error encountered running the scanner: " +
                                       "The results were null!\n");
            System.err.println("\n\nTerminating test driver...\n");
            return;
        }

        Formatter formatter = new Formatter(System.out);
        formatter.format("Number of files scanned: %d\n",
                         results.getNumFiles());
        formatter.format("Number of directories scanned: %d\n",
                         results.getNumDirectories());
        formatter.format("Total size (in B) of all scanned files: %d\n",
                         results.getTotalBytes());
        formatter.format("Average size (in B) of all scanned files: %d\n",
                         results.getAvgBytes());
    } //END main()
}
