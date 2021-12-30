package com.fantechs.common.base.agv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WarnCallbackDTO {

    @ApiModelProperty(value = "请求编号", example = "请求编号")
    private String reqCode;

    @ApiModelProperty(value = "请求时间戳", example = "yyyy-MM-dd HH:mm:ss")
    private String reqTime;

    @ApiModelProperty(value = "客户端编号", example = "客户端编号")
    private String clientCode;

    @ApiModelProperty(value = "令牌号,由调度系统颁发", example = "令牌号,由调度系统颁发")
    private String tokenCode;

    @ApiModelProperty(value = "告警内容", example = "告警内容")
    private List<WarnCallbackData> data;
}
