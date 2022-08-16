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

;
;

/**
 * 库存明细
 * wms_inner_inventory_det
 * @author mr.lei
 * @date 2021-06-02 21:04:29
 */
@Data
@Table(name = "wms_inner_inventory_det")
public class WmsInnerInventoryDet extends ValidGroup implements Serializable {
    /**
     * 库存明细ID
     */
    @ApiModelProperty(name="inventoryDetId",value = "库存明细ID")
    @Id
    @Column(name = "inventory_det_id")
    private Long inventoryDetId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="5")
    private String barcode;

    /**
     * 物料数量
     */
    @ApiModelProperty(name="materialQty",value = "物料数量")
    @Excel(name = "物料数量", height = 20, width = 30,orderNum="6")
    @Column(name = "material_qty")
    private BigDecimal materialQty;


    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="7")
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 生产批次号
     */
    @ApiModelProperty(name="productionBatchCode",value = "生产批次号")
    @Excel(name = "生产批次号", height = 20, width = 30,orderNum="8")
    @Column(name = "production_batch_code")
    private String productionBatchCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 作业状态(1-已收货、2-在库、3-已拣货、4-已出库 5-已取消)
     */
    @ApiModelProperty(name = "jobStatus",value = "作业状态(1-待收货,2-已收货、3-在库、4-已拣货、5-已出库 6-已取消)")
    @Excel(name = "作业状态(1-待收货,2-已收货、3-在库、4-已拣货、5-已出库 6-已取消)",height = 20,width = 30,orderNum = "9")
    @Column(name = "job_status")
    private Byte jobStatus;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name = "ifStockLock",value = "盘点锁(0-否 1-是)")
    @Excel(name = "盘点锁(0-否 1-是)",height = 20,width = 30,orderNum = "10")
    @Column(name = "if_stock_lock")
    private Byte ifStockLock;

    /**
     * 锁定状态(0-否 1-是)
     */
    @ApiModelProperty(name = "lockStatus",value = "锁定状态(0-否 1-是)")
    @Column(name = "lock_status")
    private Byte lockStatus;

    /**
     * 收货日期
     */
    @ApiModelProperty(name = "receivingDate",value = "收货日期")
    @Excel(name = "收货日期", height = 20, width = 30,orderNum="11")
    @Column(name = "receiving_date")
    @JSONField(name = "yyyy-MM-dd HH:mm:ss")
    private Date receivingDate;

    /**
     * 收货单号
     */
    @ApiModelProperty(name="asnCode",value = "收货单号")
    @Excel(name = "收货单号", height = 20, width = 30,orderNum="12")
    @Column(name = "asn_code")
    private String asnCode;

    /**
     * 发货日期
     */
    @ApiModelProperty(name = "deliverDate",value = "发货日期")
    @Excel(name = "发货日期", height = 20, width = 30,orderNum="13")
    @Column(name = "deliver_date")
    @JSONField(name = "yyyy-MM-dd HH:mm:ss")
    private Date deliverDate;

    /**
     * 出货单号
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "出货单号")
    @Excel(name = "出货单号", height = 20, width = 30,orderNum="14")
    @Column(name = "delivery_order_code")
    private String deliveryOrderCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态ID")
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    /**
     * 检验单号
     */
    @ApiModelProperty(name="inspectionOrderCode",value = "检验单号")
    @Column(name = "inspection_order_code")
    private String inspectionOrderCode;

    /**
     * 质检日期
     */
    @ApiModelProperty(name="qcDate",value = "质检日期")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "qc_date")
    private Date qcDate;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 条码状态(1-待收货、2-已收货、3-在库、4-已拣选、5-已复核、6-已出库、7-已取消)
     */
    @ApiModelProperty(name = "barcodeStatus",value = "条码状态(1-待收货、2-已收货、3-在库、4-已拣选、5-已复核、6-已出库、7-已取消)")
    @Column(name = "barcode_status")
    private Byte barcodeStatus;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="18",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="20",exportFormat ="yyyy-MM-dd HH:mm:ss")
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


    /**
     * 销售编码
     */
    private String option3;

    /**
     * PO号
     */
    private String option4;

    private String option5;

    private static final long serialVersionUID = 1L;

    // 20211224 bgkun

    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    @Column(name = "sales_barcode")
    private String salesBarcode;

    @ApiModelProperty(name = "customerBarcode",value = "客户条码")
    @Column(name = "customer_barcode")
    private String customerBarcode;
}