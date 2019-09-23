package com.cebon.video.ffmpeg.video.callable;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: cy
 * @description: TODO 进程输出信息打印线程
 */
@Slf4j
public class OutputMsgCallable implements Callable<Long> {

    private BufferedReader br;

    private Long taskId;

    private ConcurrentHashMap<Long,Process> taskDb;

    private OutputMsgCallable(InputStream ins, Long taskId,ConcurrentHashMap<Long,Process> taskDb){
        init(ins);
        this.taskId = taskId;
        this.taskDb = taskDb;
    }
    private void init(InputStream ins){
        this.br = new BufferedReader(new InputStreamReader(ins));
    }

    public static OutputMsgCallable create(InputStream ins, Long taskId,ConcurrentHashMap<Long,Process> taskDb){
        return new OutputMsgCallable(ins, taskId,taskDb);
    }

    @Override
    public Long call() throws Exception {
        String msg;
        //手动取消或者线程结束--退出线程
        while ((msg = br.readLine()) != null) {
            String lowMsg =  msg.toLowerCase();
            boolean isFail =lowMsg.contains("fail") || lowMsg.contains("miss") || lowMsg.contains("error");
            if (isFail){
                log.warn("进程Id：{},可能出错消息：{}", taskId, msg);
            }
            if (!isFail){
                log.info("进程Id:{}，消息:{}", taskId,msg);
            }
        }
        if (br.readLine() == null){
            log.info("进程Id：{}，执行完毕,从库中删除",taskId);
            taskDb.remove(taskId);
        }
        return taskId;
    }
}
