package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 库存日志
 * wms_inner_inventory_log
 * @author mr.lei
 * @date 2021-07-29 11:12:03
 */
@Data
@Table(name = "wms_inner_inventory_log")
public class WmsInnerInventoryLog extends ValidGroup implements Serializable {
    /**
     * 库存日志ID
     */
    @ApiModelProperty(name="inventoryLogId",value = "库存日志ID")
    @Id
    @Column(name = "inventory_log_id")
    private Long inventoryLogId;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

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
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 库存状态名称
     */
    @ApiModelProperty(name="inventoryStatusName",value = "库存状态名称")
    @Excel(name = "库存状态名称", height = 20, width = 30,orderNum="7")
    @Column(name = "inventory_status_name")
    private String inventoryStatusName;

    /**
     * 库存状态id
     */
    @Transient
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态id")
    private Long inventoryStatusId;

    /**
     * 作业类型(1-收货，2-上架，3-移位，4-拣货，5-补货，6-调整，7-盘点，8-发运)
     */
    @ApiModelProperty(name="jobOrderType",value = "作业类型(1-收货，2-上架，3-移位，4-拣货，5-补货，6-调整，7-盘点，8-发运)")
    @Excel(name = "作业类型(1-收货，2-上架，3-移位，4-拣货，5-补货，6-调整，7-盘点，8-发运)", height = 20, width = 30,orderNum="8")
    @Column(name = "job_order_type")
    private Byte jobOrderType;

    /**
     * 加减类型(1-加 2-减)
     */
    @ApiModelProperty(name="addOrSubtract",value = "加减类型(1-加 2-减)")
    @Excel(name = "加减类型(1-加 2-减)", height = 20, width = 30,orderNum="9")
    @Column(name = "add_or_subtract")
    private Byte addOrSubtract;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="10")
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="11",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 过期日期
     */
    @ApiModelProperty(name="expiredDate",value = "过期日期")
    @Excel(name = "过期日期", height = 20, width = 30,orderNum="12",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "expired_date")
    private Date expiredDate;

    /**
     * 质检日期
     */
    @ApiModelProperty(name="qcDate",value = "质检日期")
    @Excel(name = "质检日期", height = 20, width = 30,orderNum="13",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "qc_date")
    private Date qcDate;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="14")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 收货单号
     */
    @ApiModelProperty(name="asnCode",value = "收货单号")
    @Excel(name = "收货单号", height = 20, width = 30,orderNum="15")
    @Column(name = "asn_code")
    private String asnCode;

    /**
     * 发货单号
     */
    @ApiModelProperty(name="despatchOrderCode",value = "发货单号")
    @Excel(name = "发货单号", height = 20, width = 30,orderNum="16")
    @Column(name = "despatch_order_code")
    private String despatchOrderCode;

    /**
     * 托盘号
     */
    @ApiModelProperty(name="palletCode",value = "托盘号")
    @Excel(name = "托盘号", height = 20, width = 30,orderNum="17")
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Column(name = "packing_unit_name")
    private String packingUnitName;

    /**
     * 期初数量
     */
    @ApiModelProperty(name="initialQty",value = "期初数量")
    @Excel(name = "期初数量", height = 20, width = 30,type=10,numFormat="0.000",orderNum="18")
    @Column(name = "initial_qty")
    private BigDecimal initialQty;

    /**
     * 变化数量
     */
    @ApiModelProperty(name="changeQty",value = "变化数量")
    @Excel(name = "变化数量", height = 20, width = 30,type=10,numFormat="0.000",orderNum="19")
    @Column(name = "change_qty")
    private BigDecimal changeQty;

    /**
     * 期末数量
     */
    @ApiModelProperty(name="finalQty",value = "期末数量")
    @Excel(name = "期末数量", height = 20, width = 30,type=10,numFormat="0.000",orderNum="20")
    @Column(name = "final_qty")
    private BigDecimal finalQty;

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

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="21")
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 规格
     */
    @ApiModelProperty(name = "spec",value = "规格")
    @Excel(name = "规格", height = 20, width = 30,orderNum="24")
    @Column(name = "spec")
    private String spec;

    /**
     * 请购单号
     */
    @ApiModelProperty(name = "purchaseReqOrderCode",value = "请购单号")
    @Excel(name = "请购单号", height = 20, width = 30,orderNum="22")
    @Column(name = "purchase_req_order_code")
    private String purchaseReqOrderCode;

    /**
     * option1
     */
    @ApiModelProperty(name="option1",value = "option1")
    @Excel(name = "装置号", height = 20, width = 30,orderNum="25")
    private String option1;

    /**
     * option2
     */
    @ApiModelProperty(name="option2",value = "option2")
    @Excel(name = "主项号", height = 20, width = 30,orderNum="26")
    private String option2;

    /**
     * option3
     */
    @ApiModelProperty(name="option3",value = "option3")
    @Excel(name = "材料用途", height = 20, width = 30,orderNum="27")
    private String option3;

    /**
     * option4
     */
    @ApiModelProperty(name="option4",value = "option4")
    @Excel(name = "位号", height = 20, width = 30,orderNum="28")
    private String option4;

    /**
     * option5
     */
    @ApiModelProperty(name="option5",value = "option5")
    private String option5;

    private static final long serialVersionUID = 1L;
}