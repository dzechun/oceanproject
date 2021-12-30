package com.fantechs.common.base.general.dto.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/12/29
 */
@Data
public class LogUreportDto implements Serializable {

    @ApiModelProperty(name = "warningEventExecutePushLogId",value = "")
    private Long warningEventExecutePushLogId;
    /**
     * 预警事件ID
     */
    @ApiModelProperty(name = "warningEventIdCode",value = "预警事件ID")
    @Excel(name = "预警事件ID", height = 20, width = 30,orderNum="1")
    private String warningEventIdCode;

    /**
     *预警事件名称
     */
    @ApiModelProperty(name = "warningEventName",value = "预警事件名称")
    @Excel(name = "预警事件名称", height = 20, width = 30,orderNum="2")
    private String warningEventName;

    /**
     *预警事件描述
     */
    @ApiModelProperty(name = "warningEventDesc",value = "预警事件描述")
    @Excel(name = "预警事件描述", height = 20, width = 30,orderNum="3")
    private String warningEventDesc;

    /**
     *执行方式（1-Get 2-Post）
     */
    @ApiModelProperty(name = "callingMethod",value = "执行方式(1-GET 2-POST)")
    @Excel(name = "执行方式", height = 20, width = 30,orderNum="4",replace = {"1_GET","2_POST"})
    private Byte callingMethod;

    /**
     *通知方式（0、微信 1、短信 2、钉钉 3、邮件）
     */
    @ApiModelProperty(name = "notificationMethod",value = "通知方式（0、微信 1、短信 2、钉钉 3、邮件）")
    @Excel(name = "通知方式", height = 20, width = 30,orderNum="5",replace = {"0_微信","1_短信","2_钉钉","3_邮件"})
    private Byte notificationMethod;

    /**
     * 执行事件
     */
    @ApiModelProperty(name = "executeTime",value = "执行开始时间")
    @Excel(name = "执行开始时间", height = 20, width = 30,orderNum="6",format = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date executeTime;

    /**
     * 执行结束时间
     */
    @ApiModelProperty(name = "modifiedTime",value = "执行结束时间")
    @Excel(name = "执行结束时间", height = 20, width = 30,orderNum="7",format = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 执行结果(1-成功 2-失败)
     */
    @ApiModelProperty(name = "executeResult",value = "执行结果(1-成功 2-失败)")
    @Excel(name = "执行结果", height = 20, width = 30,orderNum="8",replace = {"1_成功","2_失败"})
    private Byte executeResult;

    /**
     * 推送结果(1-未推送 2-已推送)
     */
    @ApiModelProperty(name = "messagePushResult",value = "推送结果(1-未推送 2-已推送)")
    @Excel(name = "推送结果", height = 20, width = 30,orderNum="9",replace = {"1_未推送","2_已推送"})
    private Byte messagePushResult;

    /**
     * 处理结果(1-未确认 2-已确认 3-不予处理)
     */
    @ApiModelProperty(name = "handleResult",value = "处理结果(1-未确认 2-已确认 3-不予处理)")
    @Excel(name = "处理结果", height = 20, width = 30,orderNum="10",replace = {"1_未确认","2_已确认","3_不予处理"})
    private Byte handleResult;

    /**
     * 处理时间
     */
    @ApiModelProperty(name = "handleTime",value = "处理时间")
    @Excel(name = "处理时间", height = 20, width = 30,orderNum="11",format = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /**
     * 人员等级（1、一级人员 2、二级人员 3、三级人员 4、四级人员）
     */
    @ApiModelProperty(name = "personnelLevel",value = "人员等级（1、一级人员 2、二级人员 3、三级人员 4、四级人员）")
    @Excel(name = "人员等级", height = 20, width = 30,orderNum="12",replace = {"1_一级人员","2_二级人员","3_三级人员","4_四级人员"})
    private Byte personnelLevel;

    /**
     * 推送消息
     */
    @ApiModelProperty(name = "executeParameter",value = "推送消息")
    @Excel(name = "推送消息", height = 20, width = 30,orderNum="13")
    private String executeParameter;

    /**
     * 事件推送配置id
     */
    @ApiModelProperty(name = "warningPushConfigId",value = "事件推送配置id")
    private Long warningPushConfigId;
}
