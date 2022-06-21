package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2022/6/21
 */
@Data
public class NotOrderInStorage implements Serializable {

    /**
     * 物料id
     */
    @ApiModelProperty(name = "materialId",value = "物料id")
    private Long materialId;

    /**
     * 相关单号
     */
    @ApiModelProperty(name = "relevanceOrderCode",value = "相关单号")
    private String relevanceOrderCode;

    /**
     * 包装单位
     */
    @ApiModelProperty(name = "packingUnitName",value = "包装单位")
    private String packingUnitName;

    /**
     * 数量
     */
    @ApiModelProperty(name = "packingQty",value = "数量")
    private BigDecimal packingQty;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 销售条码
     */
    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String salesBarcode;
}
