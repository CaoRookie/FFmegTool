package com.cebon.video.ffmpeg.audio;

import com.cebon.video.ffmpeg.common.StrUtil;
import com.cebon.video.ffmpeg.common.process.dao.TaskDao;
import com.cebon.video.ffmpeg.common.process.dao.impl.TaskDaoImpl;
import com.cebon.video.ffmpeg.video.CommandUtil;

/**
 * @author: cy
 * @description: TODO 音频处理
 */
public class AudioUtil {

    private AudioUtil(){}

    //音频转码
    public static Long transCoding(){
        return null;
    }

    /**
     * 提取音频
     *
     * @param ffmpegPath     ffmpeg.exe路径
     * @param inputPath      输入文件
     * @param outAudioFormat 输出音频格式
     * @param outputPath     输出文件保存路径
     * @return 命令
     */
    public static Long extractAudio(String ffmpegPath, String inputPath, String outAudioFormat, String outputPath) {
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath is blank");
        }
        if (StrUtil.isBlank(inputPath)) {
            throw new IllegalArgumentException("inputPath is blank");
        }
        if (StrUtil.isBlank(outputPath)) {
            throw new IllegalArgumentException("outputPath is blank");
        }
        String cmd = CommandUtil.createExtractAudioCmd(ffmpegPath, inputPath, outAudioFormat, outputPath);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }
}
