package com.fantechs.common.base.general.entity.qms.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;

/**
 * PDA质检历史
 * qms_ht_pda_inspection
 * @author jbb
 * @date 2021-01-09 20:43:56
 */
@Data
@Table(name = "qms_ht_pda_inspection")
public class QmsHtPdaInspection implements Serializable {
    /**
     * PDA质检历史ID
     */
    @ApiModelProperty(name="htPdaInspectionDetId",value = "PDA质检历史ID")
    @Excel(name = "PDA质检历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_pda_inspection_id")
    private Long htPdaInspectionId;

    /**
     * PDA质检ID
     */
    @ApiModelProperty(name="pdaInspectionDetId",value = "PDA质检ID")
    @Excel(name = "PDA质检ID", height = 20, width = 30)
    @Column(name = "pda_inspection_id")
    private Long pdaInspectionId;

    /**
     * PDA质检单号
     */
    @ApiModelProperty(name="pdaInspectionCode",value = "PDA质检单号")
    @Excel(name = "PDA质检单号", height = 20, width = 30)
    @Column(name = "pda_inspection_code")
    private String pdaInspectionCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="")
    @Column(name = "`work_order_id`")
    private Long workOrderId;

    /**
     * 栈板ID
     */
    @ApiModelProperty(name="packageManagerId",value = "栈板ID")
    @Excel(name = "栈板ID", height = 20, width = 30,orderNum="")
    @Column(name = "package_manager_id")
    private Long packageManagerId;

    /**
     * 检验人ID
     */
    @ApiModelProperty(name="surveyorId",value = "检验人ID")
    @Excel(name = "检验人ID", height = 20, width = 30,orderNum="")
    @Column(name = "surveyor_id")
    private Long surveyorId;

    /**
     * 单据时间
     */
    @ApiModelProperty(name="documentsTime",value = "单据时间")
    @Excel(name = "单据时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "documents_time")
    private Date documentsTime;

    /**
     * 单据类型（0、首检单 1、成品检验单）
     */
    @ApiModelProperty(name="documentsType",value = "单据类型（0、首检单 1、成品检验单）")
    @Excel(name = "单据类型（0、首检单 1、成品检验单）", height = 20, width = 30,orderNum="")
    @Column(name = "documents_type")
    private Byte documentsType;

    /**
     * 操作
     */
    @ApiModelProperty(name="operation",value = "操作")
    @Excel(name = "操作", height = 20, width = 30,orderNum="")
    private String operation;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="")
    private Byte status;

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
