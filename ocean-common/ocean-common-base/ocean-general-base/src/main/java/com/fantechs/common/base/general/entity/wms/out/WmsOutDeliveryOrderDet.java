package com.fantechs.common.base.general.entity.wms.out;

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
 * 出库单明细
 * wms_out_delivery_order_det
 * @author admin
 * @date 2021-05-07 16:44:16
 */
@Data
@Table(name = "wms_out_delivery_order_det")
public class WmsOutDeliveryOrderDet extends ValidGroup implements Serializable {

    private static final long serialVersionUID = -2599212486675580628L;
    /**
     * 出库单明细ID
     */
    @ApiModelProperty(name="deliveryOrderDetId",value = "出库单明细ID")
    @Excel(name = "出库单明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "delivery_order_det_id")
    private Long deliveryOrderDetId;

    /**
     * 出库单ID
     */
    @ApiModelProperty(name="deliveryOrderId",value = "出库单ID")
    @Excel(name = "出库单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_order_id")
    private Long deliveryOrderId;

    /**
     * 订单ID
     */
    @ApiModelProperty(name="sourceOrderId",value = "订单ID")
    @Excel(name = "订单ID", height = 20, width = 30,orderNum="")
    @Column(name = "source_order_id")
    private Long sourceOrderId;

    /**
     * 订单明细ID
     */
    @ApiModelProperty(name="orderDetId",value = "订单明细ID")
    @Excel(name = "订单明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "order_det_id")
    private Long orderDetId;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态ID")
    @Excel(name = "库存状态ID", height = 20, width = 30,orderNum="")
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    /**
     * 行号
     */
    @ApiModelProperty(name="lineNumber",value = "行号")
    @Excel(name = "行号", height = 20, width = 30,orderNum="")
    @Column(name = "line_number")
    private Integer lineNumber;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Excel(name = "包装单位名称", height = 20, width = 30,orderNum="")
    @Column(name = "packing_unit_name")
    private String packingUnitName;

    /**
     * 包装数量
     */
    @ApiModelProperty(name="packingQty",value = "包装数量")
    @Excel(name = "包装数量", height = 20, width = 30,orderNum="")
    @Column(name = "packing_qty")
    private BigDecimal packingQty;

    /**
     * 拣货数量
     */
    @ApiModelProperty(name="pickingQty",value = "拣货数量")
    @Excel(name = "拣货数量", height = 20, width = 30,orderNum="")
    @Column(name = "picking_qty")
    private BigDecimal pickingQty;

    /**
     * 发货数量
     */
    @ApiModelProperty(name="dispatchQty",value = "发货数量")
    @Excel(name = "发货数量", height = 20, width = 30,orderNum="")
    @Column(name = "dispatch_qty")
    private BigDecimal dispatchQty;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 来源系统单号
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源系统单号")
    @Excel(name = "来源系统单号", height = 20, width = 30,orderNum="")
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 来源系统行号
     */
    @ApiModelProperty(name="sourceLineNumber",value = "来源系统行号")
    @Excel(name = "来源系统行号", height = 20, width = 30,orderNum="")
    @Column(name = "source_line_number")
    private String sourceLineNumber;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="")
    @Column(name = "status")
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
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

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

}