package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Data
public class WmsInnerInventoryModel implements Serializable {
    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="1")
    @Transient
    private String warehouseName;

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="2")
    @Transient
    private String storageCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="3")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="4")
    @Transient
    private String materialName;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Excel(name = "包装单位名称", height = 20, width = 30,orderNum="5")
    @Column(name = "packing_unit_name")
    private String packingUnitName;

    /**
     * 包装数量
     */
    @ApiModelProperty(name="packingQty",value = "包装数量")
    @Excel(name = "包装数量", height = 20, width = 30,orderNum="6")
    @Column(name = "packing_qty")
    private BigDecimal packingQty;

    /**
     * 包装规格
     */
    @ApiModelProperty(name="packageSpecificationQuantity",value = "包装规格")
    @Excel(name = "包装规格", height = 20, width = 30,orderNum="7")
    @Column(name = "package_specification_quantity")
    private BigDecimal packageSpecificationQuantity;

    /**
     * 库存状态名称
     */
    @ApiModelProperty(name="inventoryStatusName",value = "库存状态名称")
    @Excel(name = "库存状态名称", height = 20, width = 30,orderNum = "8")
    @Transient
    private String inventoryStatusName;

    /**
     * 锁定状态(0-否 1-是)
     */
    @ApiModelProperty(name="lockStatus",value = "锁定状态(0-否 1-是)")
    @Excel(name = "锁定状态", height = 20, width = 30,orderNum="9",replace = {"否_1","是_1"})
    @Column(name = "lock_status")
    private Byte lockStatus;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name="stockLock",value = "盘点锁(0-否 1-是)")
    @Excel(name = "盘点锁", height = 20, width = 30,orderNum="10",replace = {"否_1","是_1"})
    @Column(name = "stock_lock")
    private Byte stockLock;

    /**
     * 质检锁(0-否 1-是)
     */
    @ApiModelProperty(name="qcLock",value = "质检锁(0-否 1-是)")
    @Excel(name = "质检锁", height = 20, width = 30,orderNum="11",replace = {"否_1","是_1"})
    @Column(name = "qc_lock")
    private Byte qcLock;
}
