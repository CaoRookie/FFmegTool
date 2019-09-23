package com.cebon.video.ffmpeg.common;

/**
 * @author: cy
 * @description: TODO 字符串相关工具
 */
public class StrUtil {

    private StrUtil(){}


    /**
     * 判断字符串为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串不为空
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str){
        return str != null && !str.isEmpty();
    }


    /**
     * 空格
     * @return
     */
    public static String blankSpace(){
        return " ";
    }
}
