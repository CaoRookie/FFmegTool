package com.cebon.video.ffmpeg.common.process.dao.impl;

import com.cebon.video.ffmpeg.common.number.NumberUtil;
import com.cebon.video.ffmpeg.common.process.ExecuteUtil;
import com.cebon.video.ffmpeg.common.process.bean.Task;
import com.cebon.video.ffmpeg.common.process.dao.TaskDao;
import com.cebon.video.ffmpeg.video.callable.OutputMsgCallable;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: cy
 * @description: TODO
 */
@Slf4j
public class TaskDaoImpl implements TaskDao {

    private static final ConcurrentHashMap<Long,Process> PROCESS_MAP = new ConcurrentHashMap<>(16);

    @Override
    public Long execCommand(String cmd) {
        try {
            Process process = ExecuteUtil.execCommand(cmd);
            Long pid = NumberUtil.getOnlySerialNumber();
            PROCESS_MAP.put(pid ,process);
            OutputMsgCallable callable = OutputMsgCallable.create(process.getErrorStream(), pid ,PROCESS_MAP);
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.submit(callable);
            return pid;
        } catch (IOException e) {
            log.warn("任务发布失败：{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean cancel(Long pId) {
        Process process = findById(pId);
        if (process == null ){
            return true;
        }
        try(OutputStream os = process.getOutputStream()){
            os.write("q".getBytes());
            os.flush();
            process.waitFor(2L,TimeUnit.SECONDS);
        }catch (IOException | InterruptedException e){
            if (process.isAlive()){
                log.warn("结束任务：{}，失败：{}", e.getMessage(), e);
            }
        }
        boolean processAlive = process.isAlive();
        return !processAlive;
    }

    @Override
    public boolean interrupt(Long pId) {
        Process process = findById(pId);
        boolean isAlive = cancel(pId);
        if (!isAlive){
            process.destroy();
            process.destroyForcibly();
        }
        return process.isAlive();
    }

    @Override
    public Process findById(Long pid) {
        return PROCESS_MAP.get(pid);
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        PROCESS_MAP.forEach((k,v)->{
            Task task = Task.builder().id(k).process(v).build();
            tasks.add(task);
        });
        return tasks;
    }
}
