package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 质检单明细表
 * @date 2020-12-16 14:58:12
 */
@Data
@Table(name = "qms_quality_inspection_det")
public class QmsQualityInspectionDet extends ValidGroup implements Serializable {
    /**
     * 质检单明细ID
     */
    @ApiModelProperty(name="qualityInspectionDetId",value = "质检单明细ID")
    @Excel(name = "质检单明细ID", height = 20, width = 30)
    @Id
    @Column(name = "quality_inspection_det_id")
    private Long qualityInspectionDetId;

    /**
     * 质检单ID
     */
    @ApiModelProperty(name="qualityInspectionId",value = "质检单ID")
    @Excel(name = "质检单ID", height = 20, width = 30)
    @Column(name = "quality_inspection_id")
    @NotNull(groups = update.class,message = "质检单ID不能为空")
    private Long qualityInspectionId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30)
    @Column(name = "material_id")
    @NotNull(groups = update.class,message = "物料ID不能为空")
    private Long materialId;

    /**
     * 检验状态（0-未检验 1-检验中 2-已检验）
     */
    @ApiModelProperty(name="checkoutStatus",value = "检验状态（0-未检验 1-待检验 2-已检验）")
    @Excel(name = "检验状态（0-未检验 1-待检验 2-已检验）", height = 20, width = 30)
    @Column(name = "checkout_status")
    private Byte checkoutStatus;

    /**
     * 检验数量
     */
    @ApiModelProperty(name="checkoutQuantity",value = "检验数量")
    @Excel(name = "检验数量", height = 20, width = 30)
    @Column(name = "checkout_quantity")
    @NotNull(groups = update.class,message = "检验数量不能为空")
    private BigDecimal checkoutQuantity;

    /**
     * 合格数量
     */
    @ApiModelProperty(name="qualifiedQuantity",value = "合格数量")
    @Excel(name = "合格数量", height = 20, width = 30)
    @Column(name = "qualified_quantity")
    private BigDecimal qualifiedQuantity;

    /**
     * 检验人
     */
    @ApiModelProperty(name="surveyor",value = "检验人")
    @Excel(name = "检验人", height = 20, width = 30)
    private String surveyor;

    /**
     * 检验结果
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果")
    @Excel(name = "检验结果", height = 20, width = 30)
    @Column(name = "inspection_result")
    private Byte inspectionResult;

    /**
     * 检验开始时间
     */
    @ApiModelProperty(name="checkoutStartTime",value = "检验开始时间")
    @Excel(name = "检验开始时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "checkout_start_time")
    private Date checkoutStartTime;

    /**
     * 检验结束时间
     */
    @ApiModelProperty(name="checkoutEndTime",value = "检验结束时间")
    @Excel(name = "检验结束时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "checkout_end_time")
    private Date checkoutEndTime;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

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
