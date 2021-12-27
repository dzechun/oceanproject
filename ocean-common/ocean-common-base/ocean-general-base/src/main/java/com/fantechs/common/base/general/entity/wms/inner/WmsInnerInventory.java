package com.fantechs.common.base.general.entity.wms.inner;

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

@Data
@Table(name = "wms_inner_inventory")
public class WmsInnerInventory extends ValidGroup implements Serializable {
    /**
     * 库存ID
     */
    @ApiModelProperty(name="inventoryId",value = "库存ID")
    @Id
    @Column(name = "inventory_id")
    private Long inventoryId;

    /**
     * 父ID
     */
    @ApiModelProperty(name="parentInventoryId",value = "父ID")
    @Column(name = "parent_inventory_id")
    private Long parentInventoryId;

    /**
     * 销售编码
     */
    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Column(name = "sales_code")
    private String salesCode;

    /**
     * 制造编码
     */
    @ApiModelProperty(name="makeCode",value = "制造编码")
    @Column(name = "make_code")
    private String makeCode;

    /**
     * 货主id
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主id")
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 库位id
     */
    @ApiModelProperty(name="storageId",value = "库位id")
    @Column(name = "storage_id")
    private Long storageId;


    /**
     * 作业状态(1-正常 2-待出)
     */
    @ApiModelProperty(name="jobStatus",value = "作业状态(1正常 2待出)")
    @Excel(name = "作业状态", height = 20, width = 30,orderNum="3",replace = {"正常_1","待出_0"})
    @Column(name = "job_status")
    private Byte jobStatus;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 单据号
     */
    @ApiModelProperty(name="relevanceOrderCode",value = "单据号")
    @Excel(name = "单据号", height = 20, width = 30,orderNum="8")
    @Column(name = "relevance_order_code")
    private String relevanceOrderCode;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态ID")
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    /**
     * 锁定状态(0-否 1-是)
     */
    @ApiModelProperty(name="lockStatus",value = "锁定状态(0-否 1-是)")
    @Excel(name = "锁定状态", height = 20, width = 30,orderNum="10",replace = {"是_1","否_0"})
    @Column(name = "lock_status")
    private Byte lockStatus;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name="stockLock",value = "盘点锁(0-否 1-是)")
    @Excel(name = "盘点锁", height = 20, width = 30,orderNum="11",replace = {"是_1","否_0"})
    @Column(name = "stock_lock")
    private Byte stockLock;

    /**
     * 质检锁(0-否 1-是)
     */
//    @ApiModelProperty(name="qcLock",value = "质检锁(0-否 1-是)")
//    @Excel(name = "质检锁", height = 20, width = 30,orderNum="13",replace = {"是_1","否_0"})
//    @Column(name = "qc_lock")
//    private Byte qcLock;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Excel(name = "包装单位名称", height = 20, width = 30,orderNum="12")
    @Column(name = "packing_unit_name")
    private String packingUnitName;

    /**
     * 包装数量
     */
    @ApiModelProperty(name="packingQty",value = "包装数量")
    @Column(name = "packing_qty")
    private BigDecimal packingQty;

    /**
     * 库存总数
     */
    @ApiModelProperty(name="inventoryTotalQty",value = "库存总数")
    @Excel(name = "库存总数", height = 20, width = 30,type=10,numFormat="0.000",orderNum="13")
    @Column(name = "inventory_total_qty")
    private BigDecimal inventoryTotalQty;

    /**
     * 包装规格
     */
    @ApiModelProperty(name="packageSpecificationQuantity",value = "包装规格")
    @Column(name = "package_specification_quantity")
    private BigDecimal packageSpecificationQuantity;

    /**
     * 托盘号
     */
    @ApiModelProperty(name="palletCode",value = "托盘号")
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 收货日期
     */
    @ApiModelProperty(name="receivingDate",value = "收货日期")
    @Column(name = "receiving_date")
    private Date receivingDate;

    /**
     * 质检日期
     */
    @ApiModelProperty(name="qcDate",value = "质检日期")
    @Column(name = "qc_date")
    private Date qcDate;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,exportFormat = "yyyy-MM-dd",orderNum="15")
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 过期日期
     */
    @ApiModelProperty(name="expiredDate",value = "过期日期")
    @Excel(name = "过期日期", height = 20, width = 30,exportFormat = "yyyy-MM-dd",orderNum="16")
    @Column(name = "expired_date")
    private Date expiredDate;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="14")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 采购单号
     */
    @ApiModelProperty(name="poCode",value = "采购单号")
    @Column(name = "po_code")
    private String poCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 销售单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售单号")
    @Column(name = "sales_order_code")
    private String salesOrderCode;

    /**
     * 供应商id
     */
    @ApiModelProperty(name="supplierId",value = "供应商名称")
    @Column(name = "supplier_id")
    private Long supplierId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="19",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="21",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 任务id
     */
    @ApiModelProperty(name = "jobOrderDetId",value = "任务id")
    @Column(name = "job_order_det_id")
    private Long jobOrderDetId;

    private static final long serialVersionUID = 1L;

    /**
     * 检验单号
     */
    @ApiModelProperty(name = "inspectionOrderCode",value = "检验单号")
    @Column(name = "inspection_order_code")
    private String inspectionOrderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractCode",value = "合同号")
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 规格
     */
    @ApiModelProperty(name = "spec",value = "规格")
    @Column(name = "spec")
    private String spec;

    /**
     * 请购单号
     */
    @ApiModelProperty(name = "purchaseReqOrderCode",value = "请购单号")
    @Column(name = "purchase_req_order_code")
    private String purchaseReqOrderCode;

    private String option1;

    private String option2;

    private String option3;

    private String option4;

    private String option5;
}
