package com.fantechs.common.base.general.dto.ews.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/27
 */
@Data
public class EwsWarningUserInfoImport implements Serializable {
    /**
     * 用户工号
     */
    @ApiModelProperty(name="userCode",value = "用户工号")
    @Excel(name = "用户工号", height = 20, width = 30)
    private String userCode;
    /**
     * 微信号
     */
    @ApiModelProperty(name="wechat",value = "微信号")
    @Excel(name = "微信号", height = 20, width = 30)
    private String wechat;

    /**
     * 钉钉
     */
    @ApiModelProperty(name="dingTalk",value = "钉钉")
    @Excel(name = "钉钉", height = 20, width = 30)
    private String dingTalk;

    /**
     * 手机号
     */
    @ApiModelProperty(name="mobilePhone",value = "手机号")
    @Excel(name = "手机号", height = 20, width = 30)
    private String mobilePhone;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="eMailAddress",value = "邮箱地址")
    @Excel(name = "邮箱地址", height = 20, width = 30)
    private String mailAddress;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
