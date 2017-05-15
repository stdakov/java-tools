package helper;

import java.io.File;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path {

    public Path() {

    }

    public static void main(String[] args) {

    }

    public static String getRootPath() {
        String jarPath = null;

        try {
            String mainClassFolder = Path.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            mainClassFolder = URLDecoder.decode(mainClassFolder, "UTF-8");
            mainClassFolder = mainClassFolder.replace('/', File.separatorChar);
            //here we check if the jar is compiled by IDE. By IDE we will not have "file:"... in the begging of the path
            if (mainClassFolder.contains("file:") && mainClassFolder.contains(".jar")) {
                String regex = "file:(.*)[\\/\\\\].*\\.jar";

                Matcher matcher = Pattern.compile(regex).matcher(mainClassFolder);
                while (matcher.find()) {
                    jarPath = matcher.group(1);
                }

                //if OS is windows we have to remove first char //\C:\... should be only //C:\...
                //if (SystemUtils.IS_OS_WINDOWS) {
                if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                    // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
                    jarPath = jarPath.substring(1); //\C:\Users\Administrator\...
                }

            } else {
                String[] paths = mainClassFolder.split(File.separator + "classes" + File.separator);
                if (paths.length > 0) {
                    jarPath = paths[0];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //windows OS is buggy and returns wrong separator character from ...getPath();
        return jarPath;
    }
}