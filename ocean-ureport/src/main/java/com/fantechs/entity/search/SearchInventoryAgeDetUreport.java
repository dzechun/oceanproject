package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Data
public class SearchInventoryAgeDetUreport extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "storageId",value = "库位id")
    private Long storageId;

    @ApiModelProperty(name = "materialId",value = "物料")
    private Long materialId;

    @ApiModelProperty(name = "inventoryStatusId",value = "库存状态id")
    private Long inventoryStatusId;

    @ApiModelProperty(name = "rangeStart",value = "库龄范围起始值")
    private Integer rangeStart;

    @ApiModelProperty(name = "rangeEnd",value = "库龄范围结束值")
    private Integer rangeEnd;

    /**
     *  批次号/PO号
     */
    @ApiModelProperty(name = "samePackageCode",value = "批次号/PO号")
    private String  samePackageCode;

    /**
     *  销售编码
     */
    @ApiModelProperty(name = "salesCode",value = "销售编码")
    private String  salesCode;

    /**
     *  批次号/PO号为空（0-否 1-是）
     */
    @ApiModelProperty(name = "samePackageCodeIsNull",value = "批次号/PO号为空（0-否 1-是）")
    private Integer  samePackageCodeIsNull;

    /**
     *  销售编码为空（0-否 1-是）
     */
    @ApiModelProperty(name = "salesCodeIsNull",value = "销售编码为空（0-否 1-是）")
    private Integer  salesCodeIsNull;

    /**
     *  厂内码
     */
    @ApiModelProperty(name = "barcode",value = "厂内码")
    private String  barcode;

    /**
     *  销售条码
     */
    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String  salesBarcode;

    /**
     *  客户条码
     */
    @ApiModelProperty(name = "customerBarcode",value = "客户条码")
    private String  customerBarcode;

}
