package com.fantechs.common.base.general.dto.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfigReWui;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/27
 */
@Data
public class EwsWarningPushConfigReWuiDto extends EwsWarningPushConfigReWui implements Serializable {

    @ApiModelProperty(name = "userCode",value = "用户工号")
    private String userCode;

    @ApiModelProperty(name = "nickName",value = "用户名称")
    private String nickName;
    /**
     * 微信号
     */
    @ApiModelProperty(name="wechat",value = "微信号")
    @Excel(name = "微信号", height = 20, width = 30,orderNum="")
    private String wechat;

    /**
     * 钉钉
     */
    @ApiModelProperty(name="dingTalk",value = "钉钉")
    @Excel(name = "钉钉", height = 20, width = 30,orderNum="")
    @Column(name = "ding_talk")
    private String dingTalk;

    /**
     * 手机号
     */
    @ApiModelProperty(name="mobilePhone",value = "手机号")
    @Excel(name = "手机号", height = 20, width = 30,orderNum="")
    @Column(name = "mobile_phone")
    private String mobilePhone;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="eMailAddress",value = "邮箱地址")
    @Excel(name = "邮箱地址", height = 20, width = 30,orderNum="")
    @Column(name = "e_mail_address")
    private String eMailAddress;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
