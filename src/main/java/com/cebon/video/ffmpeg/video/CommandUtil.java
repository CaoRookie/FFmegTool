package com.cebon.video.ffmpeg.video;

import com.cebon.video.ffmpeg.common.StrUtil;
import com.cebon.video.ffmpeg.common.ValidateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: cy
 * @description: TODO 命令行构建工具
 */
@Slf4j
public class CommandUtil {

    private CommandUtil() {
    }

    /**
     * 构建本地视频转码命令
     * ffmpeg [-rtsp_transport tcp] -i sourceVideoPath [-f mp4][-vcodec copy] targetVideoPath
     *
     * @param ffmpegPath      ffmpeg.exe可执行文件的路径
     * @param inputVideoPath  源文件路径
     * @param outputVideoPath 目标文件路劲
     * @param outputFormat    目标文件编码
     * @return 目标命令
     */
    public static String createSingleTransCodingCmd(String ffmpegPath, String inputVideoPath, String outputVideoPath, String outputFormat) {

        StringBuilder cmd = new StringBuilder(ffmpegPath);
        checkIsRemoteUrl(cmd, inputVideoPath);
        cmd.append(" -i ").append(inputVideoPath);
        if (StrUtil.isNotBlank(outputFormat)) {
            cmd.append(" -f ").append(outputFormat).append(StrUtil.blankSpace());
        } else {
            cmd.append(" -vcodec copy -acodec copy ");
        }
        cmd.append(" -y ").append(outputVideoPath);
        String command = cmd.toString();
        log.info("视频转码命令：{}", command);
        return command;
    }

    /**
     * 添加字幕的命令构建
     * ffmpeg [-rtsp_transport tcp] -i sourceVideoPath -vf subtitles="srtPath" out.mp4
     *
     * @param ffmpegPath      ffmpeg.exe可执行文件的路径
     * @param inputVideoPath  视频的路径
     * @param srtPath         字幕路劲 D\\:\\\\develop_tool\\\\ffmpeg\\\\bin\\\\subtitle.srt
     * @param outputVideoPath 输出文件路劲
     * @return 命令
     */
    public static String createAddSubtitleToVideoCmd(String ffmpegPath, String inputVideoPath, String srtPath, String outputVideoPath) {
        String[] temp = srtPath.split("\\\\");
        String format1 = String.join("\\\\\\\\", temp);
        srtPath = format1.replaceFirst(":", "\\\\\\\\:");
        StringBuilder cmd = new StringBuilder(ffmpegPath);
        checkIsRemoteUrl(cmd, inputVideoPath);
        cmd.append(" -i ").append(inputVideoPath)
                .append(" -vf subtitles=\" ").append(srtPath).append("\" ")
                .append(" -y ")
                .append(outputVideoPath);
        String command = cmd.toString();
        log.info("添加字幕命令：{}", command);
        return command;
    }

    /**
     * 三画面叠加,音频取主画面的
     * ffmpeg -i video1.avi -i video2.avi -i video3.avi -filter_complex "[1:v]scale=w=376:h=344:force_original_aspect_ratio=decrease[ckout1];[2:v]scale=w=376:h=344:force_original_aspect_ratio=decrease[ckout2];[0:v][ckout1]overlay=x=W-w:y=H-h[out1];[out1][ckout2]overlay=x=0:y=H-h[out]" -map "[out]" -map 0:a video.mp4
     *
     * @param ffmpegPath  ffmpeg.exe可执行文件的路径
     * @param inputVideo1 主画面
     * @param inputVideo2 右下角画面
     * @param inputVideo3 左下角画面
     * @param width       小画面宽度
     * @param height      小画面高度
     * @param outputVideo 输出路径
     * @return 命令
     */
    public static String createSuperimposedVideoCmd(String ffmpegPath, String inputVideo1, String inputVideo2, String inputVideo3, Integer width, Integer height, String outputVideo) {
        return createSuperimposedVideoAndSubtitleCmd(ffmpegPath, inputVideo1, inputVideo2, inputVideo3, null, width, height, outputVideo);
    }

    /**
     * 三画面+字幕叠加
     * ffmpeg -i video1.avi -i video2.avi -i video3.avi -filter_complex "[1:v]scale=w=376:h=344:force_original_aspect_ratio=decrease[ckout1];[2:v]scale=w=376:h=344:force_original_aspect_ratio=decrease[ckout2];[0:v][ckout1]overlay=x=W-w:y=H-h[out1];[out1][ckout2]overlay=x=0:y=H-h[out];[out]subtitles='srPath'[output]" -map "[output]" -map 0:a video.mp4
     *
     * @param ffmpegPath  ffmpeg.exe可执行文件的路径
     * @param inputVideo1 主画面
     * @param inputVideo2 右下角画面
     * @param inputVideo3 左下角画面
     * @param srtPath     字幕
     * @param width       小画面宽度
     * @param height      小画面高度
     * @param outputVideo 输出路径
     * @return 命令
     */
    public static String createSuperimposedVideoAndSubtitleCmd(String ffmpegPath, String inputVideo1, String inputVideo2, String inputVideo3, String srtPath, Integer width, Integer height, String outputVideo) {
        StringBuilder cmd = new StringBuilder(ffmpegPath);
        checkIsRemoteUrl(cmd, inputVideo1);
        cmd.append(" -i ").append(inputVideo1);
        checkIsRemoteUrl(cmd, inputVideo2);
        cmd.append(" -i ").append(inputVideo2);
        checkIsRemoteUrl(cmd, inputVideo3);
        cmd.append(" -i ").append(inputVideo3)
                .append(" -filter_complex \"[1:v]scale=w=")
                .append(width).append(":h=").append(height)
                .append(":force_original_aspect_ratio=decrease[ckout1];[2:v]scale=w=")
                .append(width).append(":h=").append(height)
                .append(":force_original_aspect_ratio=decrease[ckout2];[0:v][ckout1]overlay=x=W-w:y=H-h[out1];[out1][ckout2]overlay=x=0:y=H-h[out]");
        if (StrUtil.isNotBlank(srtPath)){
            String[] temp = srtPath.split("\\\\");
            String format1 = String.join("\\\\", temp);
            srtPath = format1.replaceFirst(":", "\\\\:");
            cmd.append(";[out]subtitles=\'").append(srtPath).append("\'[output]\" -map \"[output]\" -map 0:a -y ")
                    .append(outputVideo);
        }else {
            cmd.append("\"  -map \"[out]\" -map 0:a -y ").append(outputVideo);
        }
        String command = cmd.toString();
        log.info("画面叠加任务命令：{}", command);
        return command;
    }

    /**
     * 切割视频
     * ffmpeg.exe -i D:\develop_tool\ffmpeg\bin\1.mp4 -c copy -f segment -r 25 -segment_time 10 D:\develop_tool\ffmpeg\bin\split_%d.mp4
     *
     * @param ffmpegPath ffmpeg.exe可执行文件的路径
     * @param inputVideo 源视频路径
     * @param second     每个单位的视频时长，秒
     * @param prex       文件名开头
     * @param suffix     文件后缀名
     * @param folderPath 保存文件夹
     * @return 命令
     */
    public static String createSplitVideoCmd(String ffmpegPath, String inputVideo, String prex, String folderPath, String suffix, Integer second) {
        StringBuilder cmd = new StringBuilder(ffmpegPath);
        checkIsRemoteUrl(cmd, inputVideo);
        cmd.append(" -i ").append(inputVideo).append(" -c copy -f segment -r 25 -segment_time ")
                .append(second).append(" -y ")
                .append(folderPath).append("/").append(prex).append("_%d.").append(suffix);
        String command = cmd.toString();
        log.info("切割视频命令：{}", command);
        return command;
    }

    /**
     * 提取音频
     * ffmpeg -i D:\develop_tool\ffmpeg\bin\1.mp4 -vn D:\develop_tool\ffmpeg\bin\apple.mp3
     *
     * @param ffmpegPath     ffmpeg.exe可执行文件的路径
     * @param inputPath      输入文件
     * @param outAudioFormat 输出音频格式
     * @param outputPath     输出文件保存路径
     * @return 命令
     */
    public static String createExtractAudioCmd(String ffmpegPath, String inputPath, String outAudioFormat, String outputPath) {
        StringBuilder cmd = new StringBuilder(ffmpegPath);
        checkIsRemoteUrl(cmd, inputPath);
        cmd.append(" -i ").append(inputPath);
        if (StrUtil.isNotBlank(outAudioFormat)) {
            cmd.append(" -f ").append(outAudioFormat);
        }
        cmd.append(" -vn -y ").append(outputPath);
        String command = cmd.toString();
        log.info("提取音频命令：{}", command);
        return command;
    }

    /**
     * 提取视频
     * ffmpeg -i D:\develop_tool\ffmpeg\bin\1.mp4 -an D:\develop_tool\ffmpeg\bin\apple.mp4
     *
     * @param ffmpegPath     ffmpeg.exe可执行文件的路径
     * @param inputPath      输入文件
     * @param outVideoFormat 输出音频格式
     * @param outputPath     输出文件保存路径
     * @return 命令
     */
    public static String createExtractVideoCmd(String ffmpegPath, String inputPath, String outVideoFormat, String outputPath) {
        StringBuilder cmd = new StringBuilder(ffmpegPath);
        checkIsRemoteUrl(cmd, inputPath);
        cmd.append(" -i ").append(inputPath);
        if (StrUtil.isNotBlank(outVideoFormat)) {
            cmd.append(" -f ").append(outVideoFormat);
        } else {
            cmd.append(" -vcodec copy ");
        }
        cmd.append(" -an -y ").append(outputPath);
        String command = cmd.toString();
        log.info("提取视频命令：{}", command);
        return command;
    }

    /**
     * 截取视频命令
     * 注意，如果输入的是M3U8文件，那么 startSecond 不能超过 8*60*60 秒，否则会出错（在FFMPEG最新版没有问题）
     * ffmpeg -ss 10 -i D:\develop_tool\ffmpeg\bin\1.mp4 -c copy -t 10  dd.mp4
     *
     * @param ffmpegPath   ffmpeg.exe可执行文件的路径
     * @param inputPath    输入文件
     * @param startSecond  从 startSecond 秒开始
     * @param duration     持续时间--秒
     * @param outputFormat 输出格式
     * @param outPath      输出文件
     * @return 命令
     */
    public static String createSubVideoCmd(String ffmpegPath, String inputPath, Integer startSecond, Integer duration, String outputFormat, String outPath) {
        StringBuilder cmd = new StringBuilder(ffmpegPath);
        if (startSecond != null && startSecond > 0) {
            cmd.append(" -ss ").append(startSecond);
        }
        cmd.append(" -i ").append(inputPath);
        if (StrUtil.isNotBlank(outputFormat)) {
            cmd.append(" -f ").append(outputFormat);
        } else {
            cmd.append(" -c copy ");
        }
        if (duration != null && duration > 0) {
            cmd.append(" -t ").append(duration);
        }
        cmd.append(" -y ").append(outPath);
        String command = cmd.toString();
        log.info("截取视频命令：{}", command);
        return command;
    }

    /**
     * 给视频添加音频
     * ffmpeg -i D:\develop_tool\ffmpeg\ffmpeg-20190918-53d31e9-win64-static\video\0.mp3 -i D:\develop_tool\ffmpeg\ffmpeg-20190918-53d31e9-win64-static\video\01.mp4 -c copy out.mp4
     *
     * @param ffmpegPath ffmpeg.exe可执行文件的路径
     * @param inputVideoPath 输入视频文件
     * @param inputAudioPath 输入音频文件
     * @param outputFormat 输出格式
     * @param outPath 输出文件
     * @return 命令
     */
    public static String createMergeVideoAndAudioCmd(String ffmpegPath, String inputVideoPath, String inputAudioPath, String outputFormat, String outPath){
        StringBuilder cmd = new StringBuilder(ffmpegPath);
        checkIsRemoteUrl(cmd, inputVideoPath);
        cmd.append(" -i ").append(inputVideoPath);
        checkIsRemoteUrl(cmd, inputAudioPath);
        cmd.append(" -i ").append(inputAudioPath);
        if (StrUtil.isNotBlank(outputFormat)){
            cmd.append(" -f ").append(outputFormat);
        }else{
            cmd.append(" -c copy ");
        }
        cmd.append(" -y ").append(outPath);
        String command = cmd.toString();
        log.info("提取视频命令：{}", command);
        return command;
    }

    private static void checkIsRemoteUrl(StringBuilder cmd, String inputVideoPath) {
        if (ValidateUtil.isRemoteVideo(inputVideoPath)) {
            cmd.append(" -rtsp_transport tcp ");
        }
    }

}
