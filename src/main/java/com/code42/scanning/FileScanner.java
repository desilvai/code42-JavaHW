/*
 * Copyright (c) 2016, Ian J. De Silva
 * All Rights Reserved
 *
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */

package com.code42.scanning;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Scan all files and directories below a specified path on a local filesystem.
 *
 * Questions:
 *   * Do I follow symlinks?  I'm assuming no for now.
 */
public class FileScanner
{
    /**
     * Scan all files and directories below a specified path on a local
     * filesystem.  This does not follow symlinks.
     * @param path  the path to scan for files.
     * @return a summary of the results of the scan.
     */
    public static ScanResult scan(String path) throws IOException
    {
        ScanResult result = new ScanResult();
        Path root = Paths.get(path);

        // Walk the directory tree and process each encountered path.
        // This will ignore symlinks, thus we have no cycles!

        // There is no good reason why I should do the map here rather than
        // as part of the addToCount.  AFAIK, either should be as efficient
        // since Java 8 optimizes functional calls.
        Files.walk(root)
             .map(Path::toFile)
             .forEach( result::addToCount );

        return result;
    }


    /**
     * An object that stores the result of scanning a given directory.
     */
    public static class ScanResult
    {
        //----------------------------------------------------------------
        //  DATA MEMBERS
        //----------------------------------------------------------------
        /**
         * The number of files scanned
         */
        int numberOfFiles = 0;

        /**
         * The number of non-files (directories) scanned
         */
        int numberOfDirectories = 0;

        /**
         * The sum of the number of bytes in each of the scanned files
         */
        long totalBytesInFiles = 0;


        //----------------------------------------------------------------
        //  CONSTRUCTORS
        //----------------------------------------------------------------
        /**
         * Constructor (default).
         *
         * This limits the visibility of the constructor so only
         * {@link FileScanner} can create new objects.
         */
        private ScanResult()
        {
            // Nothing needed here.
        }


        //----------------------------------------------------------------
        //  Public API
        //----------------------------------------------------------------
        /**
         * Gets the number of files scanned by the scanner.
         *
         * @return the number of files scanned
         */
        public int getNumFiles()
        {
            return numberOfFiles;
        }

        /**
         * Gets the number of directories scanned by the scanner.
         *
         * @return the number of directories scanned
         */
        public int getNumDirectories()
        {
            return numberOfDirectories;
        }

        /**
         * Gets the sum of the sizes of all scanned files.
         *
         * @return the total number of bytes contained within all scanned files.
         */
        public long getTotalBytes()
        {
            return totalBytesInFiles;
        }

        // TODO -- I'd like to have this throw an exception, but I don't want
        //   to change the signature and I'm not sure if the target group is
        //   ok with extending RuntimeException and throwing those.  For now,
        //   I'll just return -1.
        /**
         * Gets the average size of the files scanned.
         * @return  the average size of the scanned files (rounded down to the
         *          nearest byte).  Returns -1 if there are no files in the
         *          directory.
         */
        public long getAvgBytes()
        {
            // Don't try to divide by 0.
            if(numberOfFiles == 0)
            {
                return -1;
            }

            // Long arithmetic!  Naturally truncates.
            return totalBytesInFiles / (long) numberOfFiles;
        }


        //----------------------------------------------------------------
        //  PACKAGE METHODS (MODIFIERS)
        //----------------------------------------------------------------
        /**
         * Process a file and adds it to the scanner's counts/totals.
         *
         * This should only be used by the {@link FileScanner#scan(String)}
         * method, hence why we've made it private.
         *
         * @param file  the file object to add to the counts.
         */
        private void addToCount(File file)
        {
            if (file.isFile())
            {
                numberOfFiles++;
                totalBytesInFiles += file.length();
            }
            else if(file.isDirectory())
            {
                numberOfDirectories++;
            }
            else
            {
                // It is possible that we encounter something that is neither
                // a file or directory (since files are defined by the OS).
                // If this is the case, our assertion that we have only files
                // and directories failed.  Throw an error so we don't
                // silently ignore it.
                throw new AssertionError("Unknown File: " + file.toString());
            }
        }

    } //END ScanResult

} //END FileScanner