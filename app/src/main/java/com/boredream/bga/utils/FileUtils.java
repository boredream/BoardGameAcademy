package com.boredream.bga.utils;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;

public class FileUtils {

    /**
     * 获取App根目录
     */
    public static File getAppDir() {
        if (!hasSdcard()) return null;
        File file = new File(Environment.getExternalStorageDirectory(), "bga");
        if (!file.exists()) {
            boolean success = file.mkdirs();
            if (!success) return null;
        }
        return file;
    }

    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getFileNameWithoutExtension(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    public static void delete(File dir, FilenameFilter filenameFilter) {
        if (dir == null) return;
        File[] files = dir.listFiles(filenameFilter);
        if (files == null) return;
        for (File file : files) {
            file.delete();
        }
    }

}
