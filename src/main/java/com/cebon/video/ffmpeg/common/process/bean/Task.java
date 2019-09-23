package com.cebon.video.ffmpeg.common.process.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author: cy
 * @description: TODO 命令保存的实体
 */
@Data
@Builder
public class Task {

    private Long id;

    private Process process;
}
