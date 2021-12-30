package com.fantechs.common.base.general.dto.ews;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/12/28
 */
@Data
public class UntreatedLogDto implements Serializable {

    /**
     * 推送日志id
     */
    @ApiModelProperty(name = "warningEventExecutePushLogId",value = "推送日志id")
    private Long warningEventExecutePushLogId;

    /**
     * 是否推送
     */
    @ApiModelProperty(name = "messagePushResult",value = "是否推送")
    private Byte messagePushResult;

    /**
     * 当前推送等级
     */
    @ApiModelProperty(name = "personnelLevel",value = "当前推送等级")
    private Byte personnelLevel;

    /**
     * 处理时间
     */
    @ApiModelProperty(name = "handleTime",value = "处理时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /**
     * 时效
     */
    @ApiModelProperty(name = "aging",value = "时效")
    private int aging;

    /**
     * 执行日期
     */
    @ApiModelProperty(name = "executeTime",value = "执行日期")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date executeTime;

    /**
     * 未处理时长
     */
    @ApiModelProperty(name = "untreatedTime",value = "未处理时长")
    private int untreatedTime;

    /**
     * 执行日志id
     */
    @ApiModelProperty(name = "warningEventExecuteLogId",value = "执行日志id")
    private Long warningEventExecuteLogId;

    /**
     * 事件推送配置id
     */
    @ApiModelProperty(name = "warningPushConfigId",value = "事件推送配置id")
    private Long warningPushConfigId;

    private Long warningEventConfigId;

    /**
     * 执行参数
     */
    @ApiModelProperty(name = "executeParameter",value = "执行参数")
    private String executeParameter;

    private String notificationMethod;
}
