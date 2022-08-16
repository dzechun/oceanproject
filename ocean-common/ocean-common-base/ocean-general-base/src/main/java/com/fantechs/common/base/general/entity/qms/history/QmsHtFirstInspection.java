package com.fantechs.common.base.general.entity.qms.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * PDA首检历史表
 * qms_ht_first_inspection
 * @author jbb
 * @date 2021-01-09 20:33:54
 */
@Data
@Table(name = "qms_ht_first_inspection")
public class QmsHtFirstInspection implements Serializable {
    /**
     * PDA首检历史ID
     */
    @ApiModelProperty(name="htFirstInspectionId",value = "PDA首检历史ID")
    @Excel(name = "PDA首检历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_first_inspection_id")
    private Long htFirstInspectionId;

    /**
     * PDA首检ID
     */
    @ApiModelProperty(name="firstInspectionId",value = "PDA首检ID")
    @Excel(name = "PDA首检ID", height = 20, width = 30)
    @Column(name = "first_inspection_id")
    private Long firstInspectionId;

    /**
     * PDA首检单号
     */
    @ApiModelProperty(name="firstInspectionCode",value = "PDA首检单号")
    @Excel(name = "PDA首检单号", height = 20, width = 30)
    @Column(name = "first_inspection_code")
    private String firstInspectionCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30)
    @Column(name = "`work_order_id`")
    private Long workOrderId;

    /**
     * 挑选总数量
     */
    @ApiModelProperty(name="selectedTotalQuantity",value = "挑选总数量")
    @Excel(name = "挑选总数量", height = 20, width = 30)
    @Column(name = "selected_total_quantity")
    private BigDecimal selectedTotalQuantity;

    /**
     * 检验结果（0、未知 1、合格 2、不合格）
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果（0、未知 1、合格 2、不合格）")
    @Excel(name = "检验结果（0、未知 1、合格 2、不合格）", height = 20, width = 30)
    @Column(name = "inspection_result")
    private Byte inspectionResult;

    /**
     * 处理人ID
     */
    @ApiModelProperty(name="handler",value = "处理人ID")
    @Excel(name = "处理人ID", height = 20, width = 30)
    private Long handler;

    /**
     * 单据时间
     */
    @ApiModelProperty(name="documentsTime",value = "单据时间")
    @Excel(name = "单据时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "documents_time")
    private Date documentsTime;

    /**
     * 操作
     */
    @ApiModelProperty(name="operation",value = "操作")
    @Excel(name = "操作", height = 20, width = 30)
    private String operation;

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
