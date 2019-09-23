package com.cebon.video.ffmpeg.video;

import com.cebon.video.ffmpeg.common.FileUtil;
import com.cebon.video.ffmpeg.common.StrUtil;
import com.cebon.video.ffmpeg.common.process.bean.Task;
import com.cebon.video.ffmpeg.common.process.dao.TaskDao;
import com.cebon.video.ffmpeg.common.process.dao.impl.TaskDaoImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * @author: cy
 * @description: TODO 操作视频相关的工具类
 */
@Slf4j
public class VideoUtil {

    private VideoUtil(){}

    /**
     * 文件转码
     *
     * @param ffmpegPath      ffmpeg.exe路径
     * @param inputVideoPath  输入文件
     * @param outputVideoPath 输出文件
     * @return 任务进程号
     */
    public static Long transCoding(String ffmpegPath, String inputVideoPath, String outputVideoPath) {
        return VideoUtil.transCoding(ffmpegPath, inputVideoPath, outputVideoPath, null);
    }

    /**
     * 视频文件转码
     *
     * @param ffmpegPath      ffmpeg.exe路径
     * @param inputVideoPath  输入文件路径
     * @param outputVideoPath 输出文件路劲
     * @param outputFormat    输出文件编码
     * @return 任务进程号
     */
    public static Long transCoding(String ffmpegPath, String inputVideoPath, String outputVideoPath, String outputFormat) {
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath must is not blank");
        }
        if (StrUtil.isBlank(inputVideoPath)) {
            throw new IllegalArgumentException("inputVideoPath is blank");
        }
        if (StrUtil.isBlank(outputVideoPath)) {
            throw new IllegalArgumentException("outputVideoPath is blank");
        }
        String cmd = CommandUtil.createSingleTransCodingCmd(ffmpegPath, inputVideoPath, outputVideoPath, outputFormat);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }

    /**
     * 视频添加字幕
     *
     * @param ffmpegPath      ffmpeg.exe路径
     * @param inputVideoPath  输入视频路径
     * @param srt             字幕文件格式
     * @param outputVideoPath 输出文件路径
     * @return 任务进程号
     */
    public static Long addSubtitle(String ffmpegPath, String inputVideoPath, File srt, String outputVideoPath) {
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath is blank");
        }
        if (StrUtil.isBlank(inputVideoPath)) {
            throw new IllegalArgumentException("inputVideoPath is blank");
        }
        if (StrUtil.isBlank(outputVideoPath)) {
            throw new IllegalArgumentException("outputVideoPath is blank");
        }
        if (FileUtil.notExist(srt)) {
            throw new IllegalArgumentException("file：" + srt.getAbsolutePath() + " is not exist");
        }
        String cmd = CommandUtil.createAddSubtitleToVideoCmd(ffmpegPath,inputVideoPath, srt.getAbsolutePath(), outputVideoPath);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }

    /**
     * 合并视频
     *
     * @param ffmpegPath  ffmpeg.exe路径
     * @param inputVideo1 主画面
     * @param inputVideo2 右下角画面
     * @param inputVideo3 左下角画面
     * @param width       小画面宽度
     * @param height      小画面高度
     * @param outputVideo 输出路径
     * @return 任务进程ID
     */
    public static Long superimposedVideo(String ffmpegPath, String inputVideo1, String inputVideo2, String inputVideo3, Integer width, Integer height, String outputVideo) {
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath is blank");
        }
        if (StrUtil.isBlank(inputVideo1) || StrUtil.isBlank(inputVideo2) || StrUtil.isBlank(inputVideo3)) {
            throw new IllegalArgumentException("inputVideo is blank");
        }
        if (StrUtil.isBlank(outputVideo)) {
            throw new IllegalArgumentException("outputVideoPath is blank");
        }
        String cmd = CommandUtil.createSuperimposedVideoCmd(ffmpegPath, inputVideo1, inputVideo2, inputVideo3, width, height, outputVideo);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }

    /**
     * 三画面+字幕叠加
     *
     * @param ffmpegPath  ffmpeg.exe路径
     * @param inputVideo1 主画面
     * @param inputVideo2 右下角画面
     * @param inputVideo3 左下角画面
     * @param srtFile     字幕
     * @param width       小画面宽度
     * @param height      小画面高度
     * @param outputVideo 输出路径
     * @return 命令
     */
    public static Long superimposedVideoAndSrt(String ffmpegPath, String inputVideo1, String inputVideo2, String inputVideo3, File srtFile, Integer width, Integer height, String outputVideo) {
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath is blank");
        }
        if (StrUtil.isBlank(inputVideo1) || StrUtil.isBlank(inputVideo2) || StrUtil.isBlank(inputVideo3)) {
            throw new IllegalArgumentException("inputVideo is blank");
        }
        if (StrUtil.isBlank(outputVideo)) {
            throw new IllegalArgumentException("outputVideoPath is blank");
        }
        if (FileUtil.notExist(srtFile)) {
            throw new IllegalArgumentException("subtitle File is not exsit");
        }
        String cmd = CommandUtil.createSuperimposedVideoAndSubtitleCmd(ffmpegPath, inputVideo1, inputVideo2, inputVideo3, srtFile.getAbsolutePath(), width, height, outputVideo);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }

    /**
     * 切割视频
     *
     * @param ffmpegPath ffmpeg.exe路径
     * @param inputVideo 源视频路径
     * @param second     每个单位的视频时长，秒
     * @param prex       文件名开头
     * @param suffix     文件后缀名
     * @param folderPath 保存文件夹
     * @return 命令
     */
    public static Long splitVideo(String ffmpegPath, String inputVideo, String prex, String folderPath, String suffix, Integer second) {
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath is blank");
        }
        if (StrUtil.isBlank(inputVideo)) {
            throw new IllegalArgumentException("inputVideo is blank");
        }
        if (StrUtil.isBlank(prex)) {
            throw new IllegalArgumentException("prex is blank");
        }
        if (StrUtil.isBlank(folderPath)) {
            throw new IllegalArgumentException("folderPath is blank");
        }
        if (StrUtil.isBlank(suffix)) {
            throw new IllegalArgumentException("suffix is blank");
        }
        if (second == null || second <= 0) {
            throw new IllegalArgumentException("second must more than the zero");
        }
        String cmd = CommandUtil.createSplitVideoCmd(ffmpegPath, inputVideo, prex, folderPath, suffix, second);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }

    /**
     * 提取视频
     *
     * @param ffmpegPath     ffmpeg.exe路径
     * @param inputPath      输入文件
     * @param outVideoFormat 输出音频格式
     * @param outputPath     输出文件保存路径
     * @return 命令
     */
    public static Long extractVideo(String ffmpegPath, String inputPath, String outVideoFormat, String outputPath) {
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath is blank");
        }
        if (StrUtil.isBlank(inputPath)) {
            throw new IllegalArgumentException("inputPath is blank");
        }
        String cmd = CommandUtil.createExtractVideoCmd(ffmpegPath, inputPath, outVideoFormat, outputPath);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }

    /**
     * 截取视频命令
     * 注意，如果输入的是M3U8文件，那么 startSecond 不能超过 8*60*60 秒，否则会出错
     *
     * @param ffmpegPath   ffmpeg.exe路径
     * @param inputPath    输入文件
     * @param startSecond  从 startSecond 秒开始
     * @param duration     持续时间--秒
     * @param outputFormat 输出格式
     * @param outPath      输出文件
     * @return 任务ID
     */
    public static Long subVideo(String ffmpegPath, String inputPath, Integer startSecond, Integer duration, String outputFormat, String outPath) {
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath is blank");
        }
        if (StrUtil.isBlank(inputPath)) {
            throw new IllegalArgumentException("inputPath is blank");
        }
        if (StrUtil.isBlank(outPath)) {
            throw new IllegalArgumentException("outPath is blank");
        }
        String cmd = CommandUtil.createSubVideoCmd(ffmpegPath, inputPath, startSecond, duration, outputFormat, outPath);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }

    /**
     * 合并音视频
     * @param ffmpegPath ffmpeg.exe路径
     * @param inputVideoPath 输入视频文件
     * @param inputAudioPath 输入音频文件
     * @param outputFormat 输出格式
     * @param outPath 输出文件
     * @return 任务ID
     */
    public static Long mergeVideoAndAudio(String ffmpegPath, String inputVideoPath, String inputAudioPath, String outputFormat, String outPath){
        if (StrUtil.isBlank(ffmpegPath)) {
            throw new IllegalArgumentException("ffmpegPath is blank");
        }
        if (StrUtil.isBlank(inputVideoPath)) {
            throw new IllegalArgumentException("inputVideoPath is blank");
        }
        if (StrUtil.isBlank(inputAudioPath)) {
            throw new IllegalArgumentException("inputAudioPath is blank");
        }
        if (StrUtil.isBlank(outPath)) {
            throw new IllegalArgumentException("outPath is blank");
        }
        String cmd = CommandUtil.createMergeVideoAndAudioCmd(ffmpegPath, inputVideoPath, inputAudioPath, outputFormat, outPath);
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.execCommand(cmd);
    }


    /**
     * ffmpeg -i in.mp4 -vf scale=360:640 -acodec aac -vcodec h264 out.mp4
     *
     * 缩放
     * @return
     */
    public static Long scaleVideo(){
        return null;
    }

    /**
     * ffmpeg -i in.mp4 -strict -2 -vf crop=1080:1080:0:420 out.mp4
     *
     * 裁剪
     * @return
     */
    public static Long cropVideo(){
        return null;
    }

    /**
     * ffmpeg -i in.mp4 -vf rotate=PI/2:ow=1080:oh=1920 out.mp4
     *
     * 旋转
     * @return
     */
    public static Long rotateVideo(){
        return null;
    }

    /**
     * ffmpeg -i D:\develop_tool\ffmpeg\ffmpeg-20190918-53d31e9-win64-static\video\0.mp4 -r 5 out1.mp4
     *
     * 调节帧率
     * @return
     */
    public static Long updateFps(){return null;}

    /**
     * ffmpeg -i in.mp4
     *
     * 查看视频的详细信息
     * @return
     */
    public static Long seeVideoInfo(){
        return null;
    }



    /**
     * 中断任务，将缓冲区文件全部写入文件后，结束任务，已经生成的文件可以使用
     *
     * @param pId 任务进程号
     * @return 取消成功与否
     */
    public static boolean cancel(Long pId) {
        if (pId == null){
            throw new IllegalArgumentException("pid must not null");
        }
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.cancel(pId);
    }

    /**
     * 取消当前任务,直接Kill掉，可能已经生成的文件不能使用，损坏
     *
     * @param pId 任务进程号
     * @return 中断成功与否
     */
    public static boolean interrupt(Long pId) {
        if (pId == null){
            throw new IllegalArgumentException("pid must not null");
        }
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.interrupt(pId);
    }

    /**
     * 查询所有正在进行的任务
     * @return 任务列表
     */
    public static List<Task> findAll(){
        TaskDao taskDao = new TaskDaoImpl();
        return taskDao.findAll();
    }

    /**
     * 根据ID查询任务
     *
     * @param pId 任务ID
     * @return 任务实体
     */
    public static Task findTaskById(Long pId){
        if (pId == null){
            throw new IllegalArgumentException("pid must not null");
        }
        TaskDao taskDao = new TaskDaoImpl();
        Process process =  taskDao.findById(pId);
        return Task.builder().id(pId).process(process).build();
    }


}
