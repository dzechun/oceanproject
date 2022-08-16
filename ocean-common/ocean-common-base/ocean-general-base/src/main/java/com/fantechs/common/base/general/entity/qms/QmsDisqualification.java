package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 不合格项目表
 * @date 2021-01-06 21:16:18
 */
@Data
@Table(name = "qms_disqualification")
public class QmsDisqualification implements Serializable {
    /**
     * PDA不合格项ID
     */
    @ApiModelProperty(name="disqualificationId",value = "PDA不合格项ID")
    @Excel(name = "PDA不合格项ID", height = 20, width = 30)
    @Id
    @Column(name = "disqualification_id")
    private Long disqualificationId;

    /**
     * PDA首检明细ID
     */
    @ApiModelProperty(name="firstInspectionIdId",value = "PDA首检明细ID")
    @Excel(name = "PDA首检明细ID", height = 20, width = 30)
    @Column(name = "`first_inspection_id`")
    private Long firstInspectionIdId;

    /**
     * 不合格项等级
     */
    @ApiModelProperty(name="level",value = "不合格项等级")
    @Excel(name = "不合格项", height = 20, width = 30)
    private Long level;

    /**
     * 不合格项
     */
    @ApiModelProperty(name="disqualification",value = "不合格项")
    @Excel(name = "不合格项", height = 20, width = 30)
    private Long disqualification;

    /**
     * 检验类型（0、PDA首检 1、OOB检验）
     */
    @ApiModelProperty(name="checkoutType",value = "检验类型（0、PDA首检 1、OOB检验）")
    @Excel(name = "检验类型（0、PDA首检 1、OOB检验）", height = 20, width = 30)
    @Column(name = "checkout_type")
    private Byte checkoutType;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
