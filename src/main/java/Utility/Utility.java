package Utility;

import java.io.File;

public class Utility {
    public static boolean isFileExits(String path){
        return new File(path).isFile() && new File(path).exists();
    }
    public static File getParent(String path){
        File currentFile = new File(path);
        File parent = currentFile.getParentFile();
        return parent;
    }
}
