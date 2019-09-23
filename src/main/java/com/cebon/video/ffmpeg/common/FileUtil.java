package com.cebon.video.ffmpeg.common;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: cy
 * @description: TODO 文件工具类
 */
public class FileUtil {

    private FileUtil(){}

    /**
     * 判断文件是否存在
     *
     * @param file 目标文件
     * @return 存在与否
     */
    public static boolean exist(File file) {
        return file != null && file.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 存在与否
     */
    public static boolean exist(String filePath) {
        if (StrUtil.isBlank(filePath)){
            throw new NullPointerException(" filePath must not null!");
        }
        Path path = Paths.get(filePath);
        return Files.exists(path, LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean notExist(File file) {
        if (file == null){
            return true;
        }
        Path path = file.toPath();
        return Files.notExists(path, LinkOption.NOFOLLOW_LINKS);
    }

    public static String getResourcePath(String filename){
        // InputStream is = FileUtil.class.getResourceAsStream("ffmpeg.exe");
        return "";
    }
}
