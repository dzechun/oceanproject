package com.fantechs.common.base.general.dto.ews;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/28
 */
@Data
public class PushMessageDto implements Serializable {
    /**
     * 微信号
     */
    @ApiModelProperty(name="wechat",value = "微信号")
    private String wechat;

    /**
     * 钉钉
     */
    @ApiModelProperty(name="dingTalk",value = "钉钉")
    private String dingTalk;

    /**
     * 手机号
     */
    @ApiModelProperty(name="mobilePhone",value = "手机号")
    private String mobilePhone;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="eMailAddress",value = "邮箱地址")
    private String eMailAddress;

    /**
     * 通知方式（0、微信 1、短信 2、钉钉 3、邮件）
     */
    @ApiModelProperty(name = "notificationMethod",value = "通知方式（0、微信 1、短信 2、钉钉 3、邮件）")
    private String notificationMethod;

    /**
     * 执行参数
     */
    @ApiModelProperty(name = "executeParameter",value = "执行参数")
    private String executeParameter;
}
