package com.cebon.video.ffmpeg.common.number;

/**
 * @author: cy
 * @description: TODO
 */
public class NumberUtil {
    private NumberUtil(){}

    private final static Sequence SEQUENCE = new Sequence();


    public static Long getOnlySerialNumber (){
        return SEQUENCE.nextId();
    }
}
