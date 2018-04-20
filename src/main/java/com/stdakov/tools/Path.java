package com.stdakov.tools;

import java.io.File;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path {

    public Path() {

    }

    public static void main(String[] args) {
        System.out.println(Path.getRootPath());
    }

    public static String getRootPath() {

        String jarPath = null;
        Boolean isWindows = false;

        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            isWindows = true;
        }

        try {
            String mainClassFolder = Path.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            mainClassFolder = URLDecoder.decode(mainClassFolder, "UTF-8");

            //windows OS is buggy and returns wrong separator character from ...getPath();
            mainClassFolder = mainClassFolder.replace('/', File.separatorChar);
            //here we check if the jar is compiled by IDE. By IDE we will not have "file:"... in the begging of the path
            if (mainClassFolder.contains("file:") && mainClassFolder.contains(".jar")) {
                String regex = "file:(.*)[\\/\\\\].*\\.jar";

                Matcher matcher = Pattern.compile(regex).matcher(mainClassFolder);
                while (matcher.find()) {
                    jarPath = matcher.group(1);
                }
            } else {
                String spliter;
                if (isWindows) {
                    spliter = "\\\\classes\\\\";
                } else {
                    spliter = File.separator + "classes" + File.separator;
                }

                String[] paths = mainClassFolder.split(spliter);

                if (paths.length > 0) {
                    jarPath = paths[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if OS is windows we have to remove first char //\C:\... should be only //C:\...
        if (isWindows && jarPath != null) {
            jarPath = jarPath.substring(1);
        }

        return jarPath;
    }
}