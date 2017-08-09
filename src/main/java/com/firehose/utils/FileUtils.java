package com.firehose.utils;

import java.io.File;
import java.util.List;

/**
 * Created by rohitkumar on 03/08/17.
 */
public class FileUtils {

    private static final String DEFAULT_FORMAT = ".csv";

    /**
     * This recursive method search all files in the given directory and its
     * sub-directories and also add those files in the file list.
     *
     * @param directoryPath
     * @param fileList
     */
    public static void listfiles(String directoryPath, List<File> fileList) {
        File directory = new File(directoryPath);

        /**
         * get all the files from a directory
         */
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isDirectory()) {
                listfiles(file.getAbsolutePath(), fileList);
            }
            if (file.isFile() && file.getName().endsWith(DEFAULT_FORMAT)) {
                fileList.add(file);
            }
        }
    }

    /**
     * This recursive method search all directories in the given directory and its
     * sub-directories and also add those directories in the dirList.
     *
     * @param directoryPath
     * @param dirList
     */
    public static void listDirectories(String directoryPath, List<File> dirList) {
        File directory = new File(directoryPath);

        /**
         * get all the directories from a directory
         */
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isDirectory()) {
                dirList.add(file);
                listDirectories(file.getAbsolutePath(), dirList);
            }
        }
    }

}
