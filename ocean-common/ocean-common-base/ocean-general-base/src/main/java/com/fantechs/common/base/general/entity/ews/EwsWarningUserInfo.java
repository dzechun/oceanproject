package com.fantechs.common.base.general.entity.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 预警人员信息表
 * ews_warning_user_info
 * @author mr.lei
 * @date 2021-12-27 11:10:27
 */
@Data
@Table(name = "ews_warning_user_info")
public class EwsWarningUserInfo extends ValidGroup implements Serializable {
    /**
     * 预警人员信息表ID
     */
    @ApiModelProperty(name="warningUserInfoId",value = "预警人员信息表ID")
    @Id
    @Column(name = "warning_user_info_id")
    private Long warningUserInfoId;

    /**
     * 用户ID
     */
    @ApiModelProperty(name="userId",value = "用户ID")
    @Column(name = "user_id")
    private Long userId;

    /**
     * 微信号
     */
    @ApiModelProperty(name="wechat",value = "微信号")
    @Excel(name = "微信号", height = 20, width = 30,orderNum="3")
    private String wechat;

    /**
     * 钉钉
     */
    @ApiModelProperty(name="dingTalk",value = "钉钉")
    @Excel(name = "钉钉", height = 20, width = 30,orderNum="4")
    @Column(name = "ding_talk")
    private String dingTalk;

    /**
     * 手机号
     */
    @ApiModelProperty(name="mobilePhone",value = "手机号")
    @Excel(name = "手机号", height = 20, width = 30,orderNum="5")
    @Column(name = "mobile_phone")
    private String mobilePhone;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="eMailAddress",value = "邮箱地址")
    @Column(name = "e_mail_address")
    private String eMailAddress;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="7")
    private String remark;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除(0-删除 1-正常)
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除(0-删除 1-正常)")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}