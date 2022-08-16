package com.fantechs.common.base.general.entity.wms.inner.history;

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

@Data
@Table(name = "wms_inner_ht_inventory")
public class WmsHtInnerInventory extends ValidGroup implements Serializable {
    /**
     * 库存履历ID
     */
    @ApiModelProperty(name="htInventoryId",value = "库存履历ID")
    @Excel(name = "库存履历ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_inventory_id")
    private Long htInventoryId;

    /**
     * 库存ID
     */
    @ApiModelProperty(name="inventoryId",value = "库存ID")
    @Excel(name = "库存ID", height = 20, width = 30,orderNum="")
    @Column(name = "inventory_id")
    private Long inventoryId;

    /**
     * 父ID
     */
    @ApiModelProperty(name="parentInventoryId",value = "父ID")
    @Excel(name = "父ID", height = 20, width = 30,orderNum="")
    @Column(name = "parent_inventory_id")
    private Long parentInventoryId;

    /**
     * 销售编码
     */
    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30,orderNum="")
    @Column(name = "sales_code")
    private String salesCode;

    /**
     * 制造编码
     */
    @ApiModelProperty(name="makeCode",value = "制造编码")
    @Excel(name = "制造编码", height = 20, width = 30,orderNum="")
    @Column(name = "make_code")
    private String makeCode;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="")
    @Transient
    private String materialOwnerName;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="")
    @Transient
    private String warehouseName;

    /**
     * 库位名称
     */
    @ApiModelProperty(name="storageName",value = "库位名称")
    @Excel(name = "库位名称", height = 20, width = 30,orderNum="")
    @Transient
    private String storageName;

    /**
     * 作业状态(1-待入 2-正常 3-待出)
     */
    @ApiModelProperty(name="jobStatus",value = "作业状态(1-待入 2-正常 3-待出)")
    @Excel(name = "作业状态(1-待入 2-正常 3-待出)", height = 20, width = 30,orderNum="")
    @Column(name = "job_status")
    private Byte jobStatus;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="")
    @Transient
    private String materialName;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relevanceOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="")
    @Column(name = "relevance_order_code")
    private String relevanceOrderCode;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态ID")
    @Excel(name = "库存状态ID", height = 20, width = 30,orderNum="")
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    /**
     * 锁定状态(0-否 1-是)
     */
    @ApiModelProperty(name="lockStatus",value = "锁定状态(0-否 1-是)")
    @Excel(name = "锁定状态(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "lock_status")
    private Byte lockStatus;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name="stockLock",value = "盘点锁(0-否 1-是)")
    @Excel(name = "盘点锁(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "stock_lock")
    private Byte stockLock;

    /**
     * 质检锁(0-否 1-是)
     */
    @ApiModelProperty(name="qcLock",value = "质检锁(0-否 1-是)")
    @Excel(name = "质检锁(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "qc_lock")
    private Byte qcLock;

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
     * 库存总数
     */
    @ApiModelProperty(name="inventoryTotalQty",value = "库存总数")
    @Excel(name = "库存总数", height = 20, width = 30,orderNum="")
    @Column(name = "inventory_total_qty")
    private BigDecimal inventoryTotalQty;

    /**
     * 包装规格
     */
    @ApiModelProperty(name="packageSpecificationQuantity",value = "包装规格")
    @Excel(name = "包装规格", height = 20, width = 30,orderNum="")
    @Column(name = "package_specification_quantity")
    private BigDecimal packageSpecificationQuantity;

    /**
     * 托盘号
     */
    @ApiModelProperty(name="palletCode",value = "托盘号")
    @Excel(name = "托盘号", height = 20, width = 30,orderNum="")
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 收货日期
     */
    @ApiModelProperty(name="receivingDate",value = "收货日期")
    @Excel(name = "收货日期", height = 20, width = 30,orderNum="")
    @Column(name = "receiving_date")
    private Date receivingDate;

    /**
     * 质检日期
     */
    @ApiModelProperty(name="qcDate",value = "质检日期")
    @Excel(name = "质检日期", height = 20, width = 30,orderNum="")
    @Column(name = "qc_date")
    private Date qcDate;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="")
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 过期日期
     */
    @ApiModelProperty(name="expiredDate",value = "过期日期")
    @Excel(name = "过期日期", height = 20, width = 30,orderNum="")
    @Column(name = "expired_date")
    private Date expiredDate;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 采购单号
     */
    @ApiModelProperty(name="poCode",value = "采购单号")
    @Excel(name = "采购单号", height = 20, width = 30,orderNum="")
    @Column(name = "po_code")
    private String poCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 销售单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售单号")
    @Excel(name = "销售单号", height = 20, width = 30,orderNum="")
    @Column(name = "sales_order_code")
    private String salesOrderCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="")
    @Transient
    private String supplierName;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="")
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

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    @Transient
    private String organizationName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    @Excel(name = "创建人名称", height = 20, width = 30)
    @Transient
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30)
    @Transient
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}
