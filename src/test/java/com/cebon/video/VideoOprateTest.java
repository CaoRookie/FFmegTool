package com.cebon.video;

import com.cebon.video.ffmpeg.audio.AudioUtil;
import com.cebon.video.ffmpeg.common.FileUtil;
import com.cebon.video.ffmpeg.common.number.Sequence;
import com.cebon.video.ffmpeg.common.process.bean.Task;
import com.cebon.video.ffmpeg.video.VideoUtil;
import org.junit.Test;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author: cy
 * @description: TODO
 */
public class VideoOprateTest {

    @Test
    public void testTransCoding() throws IOException {
        Long pid = VideoUtil.transCoding("D:\\develop_tool\\ffmpeg\\bin\\ffmpeg.exe","rtsp://admin:61332433@192.168.99.116"
                ,"D:\\develop_tool\\ffmpeg\\bin\\video\\clist.mp4","mp4");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean is = VideoUtil.cancel(pid);
        System.out.println(is);

        List<Task> all = VideoUtil.findAll();
        System.out.println(all);

    }
    @Test
    public void testAddSrt(){
        File file = new File("D:\\develop_tool\\ffmpeg\\bin\\subtitle.srt");
        VideoUtil.addSubtitle("D:\\develop_tool\\ffmpeg\\bin\\ffmpeg.exe"
                ,"D:\\develop_tool\\ffmpeg\\bin\\video\\clist.mp4",file
                ,"D:\\develop_tool\\ffmpeg\\bin\\video\\subVideo.mp4");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Task> all = VideoUtil.findAll();
        System.out.println(all);
    }
    @Test
    public void testComposeVideo(){
        File file = new File("D:\\develop_tool\\ffmpeg\\bin\\subtitle.srt");
        long pid = VideoUtil.superimposedVideo("D:\\develop_tool\\ffmpeg\\bin\\ffmpeg.exe"
                ,"rtsp://admin:61332433@192.168.99.116:554/cam/realmonitor?channel=1^&subtype=0"
                ,"rtsp://admin:61332433@192.168.99.115:554/cam/realmonitor?channel=1^&subtype=1"
                ,"rtsp://admin:61332433@192.168.99.114:554/cam/realmonitor?channel=1^&subtype=1"
                ,376,344,"D:\\develop_tool\\ffmpeg\\bin\\video\\subVideo.mp4");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean is = VideoUtil.cancel(pid);
        System.out.println(is);

        List<Task> all = VideoUtil.findAll();
        System.out.println(all);

    }

    @Test
    public void testSplitVideo(){
        long pid = VideoUtil.splitVideo("D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\bin\\ffmpeg"
                ,"D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\bin\\playlist.m3u8"
                ,"myVideo","D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\bin","mp4",20);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean is = VideoUtil.cancel(pid);
        System.out.println(is);

        List<Task> all = VideoUtil.findAll();
        System.out.println(all);
    }

    @Test
    public void testSubVideo(){
        long pid = VideoUtil.subVideo("D:\\develop_tool\\ffmpeg\\bin\\ffmpeg.exe"
                ,"D:\\develop_tool\\ffmpeg\\bin\\video\\clist.mp4"
                ,2,5,"mp4"
                ,"D:\\develop_tool\\ffmpeg\\bin\\video\\sub.mp4");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Task> all = VideoUtil.findAll();
        System.out.println(all);
    }
    @Test
    public void testGetAudio(){

        long pid = AudioUtil.extractAudio("D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\bin\\ffmpeg","D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\video\\0.mp4"
                ,"mp3","D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\video\\0.mp3");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Task> all = VideoUtil.findAll();
        System.out.println(all);
    }
    @Test
    public void testGetVideo(){
        long pid = VideoUtil.extractVideo("D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\bin\\ffmpeg","D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\video\\0.mp4"
                ,null,"D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\video\\01.mp4");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Task> all = VideoUtil.findAll();
        System.out.println(all);
    }
    @Test
    public void testMergeVideoAndAudio(){

        long pid = VideoUtil.mergeVideoAndAudio("D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\bin\\ffmpeg"
                ,"D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\video\\01.mp4"
                ,"D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\video\\0.mp3"
                ,null
                ,"D:\\develop_tool\\ffmpeg\\ffmpeg-20190918-53d31e9-win64-static\\video\\xx.mp4");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Task> all = VideoUtil.findAll();
        System.out.println(all);
    }
}
