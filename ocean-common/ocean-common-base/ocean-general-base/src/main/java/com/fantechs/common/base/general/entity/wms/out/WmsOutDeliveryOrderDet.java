package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    @NotNull(message = "仓库不能为空")
    private Long warehouseId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Column(name = "storage_id")
    @NotNull(message = "库位不能为空")
    private Long storageId;

    /**
     * 月台id
     */
    @ApiModelProperty(name = "platformId",value = "月台")
    @Column(name = "platform_id")
    private Long platformId;

    /**
     * 拣货库位
     */
    @ApiModelProperty(name = "pickingStorageId",value = "拣货库位")
    @Column(name = "picking_storage_id")
    private Long pickingStorageId;

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
    @NotNull(message = "物料不能为空")
    private Long materialId;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Excel(name = "包装单位名称", height = 20, width = 30,orderNum="")
    @Column(name = "packing_unit_name")
    @NotBlank(message = "包装单位名称不能为空")
    private String packingUnitName;

    /**
     * 包装数量
     */
    @ApiModelProperty(name="packingQty",value = "包装数量")
    @Excel(name = "包装数量", height = 20, width = 30,orderNum="")
    @Column(name = "packing_qty")
    @NotNull(message = "包装数量不能为空")
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
     * 管线号
     */
    @ApiModelProperty(name="pipelineNumber",value = "管线号")
    @Excel(name = "管线号", height = 20, width = 30,orderNum="")
    @Column(name = "pipeline_number")
    private String pipelineNumber;

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


    // ===== 20211025 增加
    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30,orderNum="")
    @Column(name = "sales_code")
    private String salesCode;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    /**
     * 扩展字段4
     */
    @ApiModelProperty(name="option4",value = "扩展字段4")
    private String option4;

    /**
     * 扩展字段5
     */
    @ApiModelProperty(name="option5",value = "扩展字段5")
    private String option5;

    /**
     * 扩展字段6
     */
    @ApiModelProperty(name="option6",value = "扩展字段6")
    private String option6;

    /**
     * 扩展字段7
     */
    @ApiModelProperty(name="option7",value = "扩展字段7")
    private String option7;

    /**
     * 扩展字段8
     */
    @ApiModelProperty(name="option8",value = "扩展字段8")
    private String option8;

    /**
     * 扩展字段9
     */
    @ApiModelProperty(name="option9",value = "扩展字段9")
    private String option9;

    /**
     * 扩展字段10
     */
    @ApiModelProperty(name="option10",value = "扩展字段10")
    private String option10;

    /**
     * 扩展字段11
     */
    @ApiModelProperty(name="option11",value = "扩展字段11")
    private String option11;

    /**
     * 扩展字段12
     */
    @ApiModelProperty(name="option12",value = "扩展字段12")
    private String option12;
}