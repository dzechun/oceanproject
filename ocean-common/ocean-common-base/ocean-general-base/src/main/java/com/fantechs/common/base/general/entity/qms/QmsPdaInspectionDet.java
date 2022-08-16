package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * PDA质检明细
 * @date 2021-01-07 20:01:55
 */
@Data
@Table(name = "qms_pda_inspection_det")
public class QmsPdaInspectionDet extends ValidGroup implements Serializable {
    /**
     * PDA质检明细ID
     */
    @ApiModelProperty(name="pdaInspectionDetId",value = "PDA质检明细ID")
    @Excel(name = "PDA质检明细ID", height = 20, width = 30)
    @Id
    @Column(name = "pda_inspection_det_id")
    private Long pdaInspectionDetId;

    /**
     * PDA质检ID
     */
    @ApiModelProperty(name="pdaInspectionId",value = "PDA质检ID")
    @Excel(name = "PDA质检ID", height = 20, width = 30)
    @Column(name = "pda_inspection_id")
    private Long pdaInspectionId;

    /**
     * 箱码ID
     */
    @ApiModelProperty(name="packageManagerId",value = "箱码ID")
    @Excel(name = "箱码ID", height = 20, width = 30)
    @Column(name = "package_manager_id")
    private Long packageManagerId;

    /**
     * 检验数量
     */
    @ApiModelProperty(name="inspectionQuantity",value = "检验数量")
    @Excel(name = "检验数量", height = 20, width = 30)
    @Column(name = "inspection_quantity")
    private BigDecimal inspectionQuantity;

    /**
     * 合格数量
     */
    @ApiModelProperty(name="qualifiedQuantity",value = "合格数量")
    @Excel(name = "合格数量", height = 20, width = 30)
    @Column(name = "qualified_quantity")
    private BigDecimal qualifiedQuantity;

    /**
     * 检验结果（0、未知 1、合格 2、不合格）
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果（0、未知 1、合格 2、不合格）")
    @Excel(name = "检验结果（0、未知 1、合格 2、不合格）", height = 20, width = 30)
    @Column(name = "inspection_result")
    private Byte inspectionResult;

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

    /**
     * 不良项目对象集合
     */
    @ApiModelProperty(name="isDelete",value = "不良项目集合")
    private List<QmsDisqualification> list;

    private static final long serialVersionUID = 1L;
}
