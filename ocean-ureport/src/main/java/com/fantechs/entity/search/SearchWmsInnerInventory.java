package com.fantechs.entity.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Data
public class SearchWmsInnerInventory extends BaseQuery {
    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    private String storageCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    private String packingUnitName;

    /**
     * 包装数量
     */
    @ApiModelProperty(name="packingQty",value = "包装数量")
    private BigDecimal packingQty;

    /**
     * 包装规格
     */
    @ApiModelProperty(name="packageSpecificationQuantity",value = "包装规格")
    private BigDecimal packageSpecificationQuantity;

    /**
     * 库存状态名称
     */
    @ApiModelProperty(name="inventoryStatusName",value = "库存状态名称")
    private String inventoryStatusName;

    /**
     * 锁定状态(0-否 1-是)
     */
    @ApiModelProperty(name="lockStatus",value = "锁定状态(0-否 1-是)")
    private Byte lockStatus;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name="stockLock",value = "盘点锁(0-否 1-是)")
    private Byte stockLock;

    /**
     * 质检锁(0-否 1-是)
     */
    @ApiModelProperty(name="qcLock",value = "质检锁(0-否 1-是)")
    private Byte qcLock;

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractCode",value = "合同号")
    private String contractCode;

    /**
     * 规格
     */
    @ApiModelProperty(name = "spec",value = "规格")
    private String spec;

    /**
     * 请购单号
     */
    @ApiModelProperty(name = "purchaseReqOrderCode",value = "请购单号")
    private String purchaseReqOrderCode;

    private String option1;

    private String option2;

    private String option3;

    private String option4;
}
