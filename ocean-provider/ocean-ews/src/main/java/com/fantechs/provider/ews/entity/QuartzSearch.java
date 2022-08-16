package com.fantechs.provider.ews.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/12 16:14
 * @Description:
 * @Version: 1.0
 */
@Data
public class QuartzSearch implements Serializable {
    @ApiModelProperty(value = "任务调度名称",example = "任务调度名称")
    private String name;
    @ApiModelProperty(value = "被执行完整接口",example = "被执行完整接口")
    private String url;
    @ApiModelProperty(value = "调用方法（1、GET 2、POST）",example = "1")
    private String method;
    @ApiModelProperty(value = "调度规则CRON",example = "0/1 * * * * ?")
    private String cron;
    @ApiModelProperty(value = "传递参数",example = "Map<String,Object>")
    private Map<String,Object> map;
}
