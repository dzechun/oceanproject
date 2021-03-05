package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 调拨单明细
 * wms_inner_transfer_slip_det
 * @author 53203
 * @date 2021-03-05 10:50:49
 */
@Data
@Table(name = "wms_inner_transfer_slip_det")
public class WmsInnerTransferSlipDet extends ValidGroup implements Serializable {
    /**
     * 调拨单明细ID
     */
    @ApiModelProperty(name="transferSlipDetId",value = "调拨单明细ID")
    @Excel(name = "调拨单明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "transfer_slip_det_id")
    private Long transferSlipDetId;

    /**
     * 调拨单ID
     */
    @ApiModelProperty(name="transferSlipId",value = "调拨单ID")
    @Excel(name = "调拨单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_slip_id")
    private Long transferSlipId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 调出储位ID
     */
    @ApiModelProperty(name="outStorageId",value = "调出储位ID")
    @Excel(name = "调出储位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "out_storage_id")
    private Long outStorageId;

    /**
     * 调入储位ID
     */
    @ApiModelProperty(name="inStorageId",value = "调入储位ID")
    @Excel(name = "调入储位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "in_storage_id")
    private Long inStorageId;

    /**
     * 计划调拨箱数
     */
    @ApiModelProperty(name="planCartonQty",value = "计划调拨箱数")
    @Excel(name = "计划调拨箱数", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_carton_qty")
    private BigDecimal planCartonQty;

    /**
     * 计划调拨总数
     */
    @ApiModelProperty(name="planTotalQty",value = "计划调拨总数")
    @Excel(name = "计划调拨总数", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_total_qty")
    private BigDecimal planTotalQty;

    /**
     * 实际调拨箱数
     */
    @ApiModelProperty(name="realityCartonQty",value = "实际调拨箱数")
    @Excel(name = "实际调拨箱数", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_carton_qty")
    private BigDecimal realityCartonQty;

    /**
     * 实际调拨总数
     */
    @ApiModelProperty(name="realityTotalQty",value = "实际调拨总数")
    @Excel(name = "实际调拨总数", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_total_qty")
    private BigDecimal realityTotalQty;

    /**
     * 单据状态（0-待调拨 1-调拨中 2-调拨完成）
     */
    @ApiModelProperty(name="transferSlipStatus",value = "单据状态（0-待调拨 1-调拨中 2-调拨完成）")
    @Excel(name = "单据状态（0-待调拨 1-调拨中 2-调拨完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_slip_status")
    private Byte transferSlipStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
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
    @Column(name = "organization_id")
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