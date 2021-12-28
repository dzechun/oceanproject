package com.fantechs.common.base.agv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarnCallbackData {

    @ApiModelProperty(value = "AGV编号", example = "AGV编号")
    private String robotCode;

    @ApiModelProperty(value = "告警开始时间", example = "告警开始时间")
    private String beginTime;

    @ApiModelProperty(value = "告警内容", example = "告警内容")
    private String warnContent;

    @ApiModelProperty(value = "任务单号", example = "当前任务单号")
    private String taskCode;
}
