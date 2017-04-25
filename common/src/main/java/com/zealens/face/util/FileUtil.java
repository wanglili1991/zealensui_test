package com.zealens.face.util;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/3/28
 * in BlaBla by Kyle
 */

public class FileUtil {
    public static void replaceCertainLineInFileLine(File file, String containsString, String newLine) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        while ((line = br.readLine()) != null) {
            if (line.contains(containsString))
                line = newLine;
            line += "\n";
            lines.add(line);
        }
        fr.close();
        br.close();

        FileWriter fw = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fw);
        for (String s : lines)
            out.write(s);
        out.flush();
        out.close();
    }

    public static String renameFileWithApacheApi(String oldName, String newName) {
        try {
            FileUtils.moveFile(new File(oldName), new File(newName));
            return newName;
        } catch (IOException e) {
            e.printStackTrace();
            return oldName;
        }
    }

    public static void copyFileUsingApacheApi(File source, File dest) {
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDirectoryUsingApacheApi(File source, File dest) {
        try {
            FileUtils.copyDirectory(source, dest, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileNameFromAbsolutePath(String path) {
        int index = path.lastIndexOf(File.separator);
        return path.substring(index + 1);
    }

    public static void appendContentToFile(String fileName, String data) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            EndingUtil.closeSilently(bw, fw);
        }
    }
}
