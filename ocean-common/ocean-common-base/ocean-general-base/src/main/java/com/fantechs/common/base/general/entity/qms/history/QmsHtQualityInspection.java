package com.fantechs.common.base.general.entity.qms.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 质检单历史表
 * @date 2020-12-16 11:36:34
 */
@Data
@Table(name = "qms_ht_quality_inspection")
public class QmsHtQualityInspection implements Serializable {
    /**
     * 质检单历史ID
     */
    @ApiModelProperty(name="htQualityInspectionId",value = "质检单历史ID")
    @Excel(name = "质检单历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_quality_inspection_id")
    private Long htQualityInspectionId;

    /**
     * 质检单ID
     */
    @ApiModelProperty(name="qualityInspectionId",value = "质检单ID")
    @Excel(name = "质检单ID", height = 20, width = 30)
    @Column(name = "quality_inspection_id")
    private Long qualityInspectionId;

    /**
     * 质检单号
     */
    @ApiModelProperty(name="qualityInspectionCode",value = "质检单号")
    @Excel(name = "质检单号", height = 20, width = 30)
    @Column(name = "quality_inspection_code")
    private String qualityInspectionCode;

    /**
     * 单据状态（0-未检验 1-检验中 2-已检验）
     */
    @ApiModelProperty(name="billsStatus",value = "单据状态（0-未检验 1-检验中 2-已检验）")
    @Excel(name = "单据状态（0-未检验 1-检验中 2-已检验）", height = 20, width = 30)
    @Column(name = "bills_status")
    private Byte billsStatus;

    /**
     * 单据类型
     */
    @ApiModelProperty(name="billsType",value = "单据类型")
    @Excel(name = "单据类型", height = 20, width = 30)
    @Column(name = "bills_type")
    private Long billsType;

    /**
     * 检验区域
     */
    @ApiModelProperty(name="examinationRegion",value = "检验区域")
    @Excel(name = "检验区域", height = 20, width = 30)
    @Column(name = "examination_region")
    private String examinationRegion;

    /**
     * 计划检验时间
     */
    @ApiModelProperty(name="provingTime",value = "计划检验时间")
    @Excel(name = "计划检验时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "proving_time")
    private Date provingTime;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

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
