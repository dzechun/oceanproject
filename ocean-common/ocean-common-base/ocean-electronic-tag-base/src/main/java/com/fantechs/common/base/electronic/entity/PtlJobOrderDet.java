package com.fantechs.common.base.electronic.entity;

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

;
;

/**
 * PTL-作业单明细
 * ptl_job_order_det
 * @author 86178
 * @date 2021-06-01 17:06:58
 */
@Data
@Table(name = "ptl_job_order_det")
public class PtlJobOrderDet extends ValidGroup implements Serializable {
    /**
     * 作业单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "作业单明细ID")
    @Id
    @Column(name = "job_order_det_id")
    private Long jobOrderDetId;

    /**
     * 作业单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="3")
    @Column(name = "storage_code")
    private String storageCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="4")
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="5")
    @Column(name = "material_name")
    private String materialName;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="unitName",value = "包装单位名称")
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 整或者零(0-零 1-整)
     */
    @ApiModelProperty(name="wholeOrScattered",value = "整或者零(0-零 1-整)")
    @Excel(name = "整或者零(0-零 1-整)", height = 20, width = 30,orderNum="12")
    @Column(name = "whole_or_scattered")
    private Byte wholeOrScattered;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;

    /**
     * 整件数量
     */
    @ApiModelProperty(name="wholeQty",value = "整件数量")
    @Excel(name = "整件数量", height = 20, width = 30,orderNum="8")
    @Column(name = "whole_qty")
    private BigDecimal wholeQty;

    /**
     * 整件单位
     */
    @ApiModelProperty(name="wholeUnitName",value = "整件单位")
    @Excel(name = "整件单位", height = 20, width = 30,orderNum="9")
    @Column(name = "whole_unit_name")
    private String wholeUnitName;

    /**
     * 散件数量
     */
    @ApiModelProperty(name="scatteredQty",value = "散件数量")
    @Excel(name = "散件数量", height = 20, width = 30,orderNum="6")
    @Column(name = "scattered_qty")
    private BigDecimal scatteredQty;

    /**
     * 散件单位
     */
    @ApiModelProperty(name="scatteredUnitName",value = "散件单位")
    @Excel(name = "散件单位", height = 20, width = 30,orderNum="7")
    @Column(name = "scattered_unit_name")
    private String scatteredUnitName;

    /**
     * 规格
     */
    @ApiModelProperty(name="spec",value = "规格")
    @Excel(name = "规格", height = 20, width = 30,orderNum="11")
    @Column(name = "spec")
    private String spec;

    /**
     * 作业状态(1-待作业 2-作业中 3-已完成 4-挂起)
     */
    @ApiModelProperty(name="jobStatus",value = "作业状态(1-待作业 2-作业中 3-已完成 4-挂起)")
    @Excel(name = "作业状态(1-待作业 2-作业中 3-已完成 4-挂起)", height = 20, width = 30,orderNum="10")
    @Column(name = "job_status")
    private Byte jobStatus;

    /**
     * 是否挂起(0-否 1-挂起)
     */
    @ApiModelProperty(name="ifHangUp",value = "是否挂起(0-否 1-挂起)")
    @Column(name = "if_hang_up")
    private Byte ifHangUp;

    /**
     * 拍灯时间
     */
    @ApiModelProperty(name="lightOutTime",value = "拍灯时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "light_out_time")
    private Date lightOutTime;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}