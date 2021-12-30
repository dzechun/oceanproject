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
     * 来料条码表ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码表ID")
    @Column(name = "material_barcode_id")
    private Long materialBarcodeId;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name = "ifStockLock",value = "盘点锁(0-否 1-是)")
    @Column(name = "if_stock_lock")
    private Byte ifStockLock;

    /**
     * 入库日期
     */
    @ApiModelProperty(name = "receivingDate",value = "入库日期")
    @Excel(name = "入库日期", height = 20, width = 30,orderNum="17")
    @Column(name = "receiving_date")
    @JSONField(name = "yyyy-MM-dd HH:mm:ss")
    private Date receivingDate;

    /**
     * 入库单号
     */
    @ApiModelProperty(name="asnCode",value = "入库单号")
    @Excel(name = "入库单号", height = 20, width = 30,orderNum="16")
    @Column(name = "asn_code")
    private String asnCode;

    /**
     * 出库日期
     */
    @ApiModelProperty(name = "deliverDate",value = "出库日期")
    @Excel(name = "出库日期", height = 20, width = 30,orderNum="19")
    @Column(name = "deliver_date")
    @JSONField(name = "yyyy-MM-dd HH:mm:ss")
    private Date deliverDate;

    /**
     * 出库单号
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "出库单号")
    @Excel(name = "出货单号", height = 20, width = 30,orderNum="18")
    @Column(name = "delivery_order_code")
    private String deliveryOrderCode;


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
     * 条码状态(1-在库、2-已出库、3-已取消)
     */
    @ApiModelProperty(name = "barcodeStatus",value = "条码状态(1-在库、2-已出库、3-已取消)")
    @Excel(name = "条码状态(1-在库、2-已出库、3-已取消)", height = 20, width = 30,orderNum="5")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="21",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="23",exportFormat ="yyyy-MM-dd HH:mm:ss")
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

    private static final long serialVersionUID = 1L;
}