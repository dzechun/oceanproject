package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 预警人员关系表
 * base_warning_personnel
 * @author 53203
 * @date 2021-03-03 15:08:34
 */
@Data
@Table(name = "base_warning_personnel")
public class BaseWarningPersonnel extends ValidGroup implements Serializable {
    /**
     * 预警人员关系ID
     */
    @ApiModelProperty(name="warningPersonnelId",value = "预警人员关系ID")
    @Excel(name = "预警人员关系ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "warning_personnel_id")
    private Long warningPersonnelId;

    /**
     * 预警ID
     */
    @ApiModelProperty(name="warningId",value = "预警ID")
    @Excel(name = "预警ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_id")
    private Long warningId;

    /**
     * 人员id
     */
    @ApiModelProperty(name="personnelId",value = "人员id")
    @Excel(name = "人员id", height = 20, width = 30,orderNum="") 
    @Column(name = "personnel_id")
    private Long personnelId;

    /**
     * 微信号
     */
    @ApiModelProperty(name="wechat",value = "微信号")
    @Excel(name = "微信号", height = 20, width = 30,orderNum="")
    @Column(name = "wechat")
    private String wechat;

    /**
     * 钉钉
     */
    @ApiModelProperty(name="dingTalk",value = "钉钉")
    @Excel(name = "钉钉", height = 20, width = 30,orderNum="") 
    @Column(name = "ding_talk")
    private String dingTalk;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}