package com.cebon.video.ffmpeg.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: cy
 * @description: TODO
 */
public class ValidateUtil {

    private ValidateUtil(){}

    public static boolean isRemoteVideo(String url){
        String pattern = "^(https?|rtsp|rtmp)://.+$";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(url);
        return matcher.matches();
    }

}
