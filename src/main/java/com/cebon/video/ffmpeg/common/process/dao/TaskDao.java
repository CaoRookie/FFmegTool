package com.cebon.video.ffmpeg.common.process.dao;

import com.cebon.video.ffmpeg.common.process.bean.Task;

import java.util.List;

/**
 * @author: cy
 * @description: TODO
 */
public interface TaskDao {

    /**
     * 提交命令
     * @param cmd 命令
     * @return 任务进程号
     */
    Long execCommand(String cmd);

    /**
     * 结束任务
     * @param pId 任务进程号
     * @return 结束状态，成功返回 true
     */
    boolean cancel(Long pId);

    /**
     * 中断任务
     * @param pId 任务ID
     * @return 中断状态
     */
    boolean interrupt(Long pId);

    /**
     * 查询进程
     * @param pid 任务Id
     * @return 进程实例
     */
    Process findById(Long pid);

    /**
     * 查询正在进行的所有任务
     * @return 任务列表
     */
    List<Task> findAll();


}
