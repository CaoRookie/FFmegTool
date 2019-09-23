package com.cebon.video.ffmpeg.common.process;

import java.io.IOException;

/**
 * @author: cy
 * @description: TODO 任务提交工具类
 */
public class ExecuteUtil {

    /**
     * 执行命令行并获取进程
     * @param cmd 命令
     * @return 命令相对的进程
     * @throws IOException 输入/输出异常
     */
    public static Process execCommand(String cmd) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        return runtime.exec(cmd);
    }
}
