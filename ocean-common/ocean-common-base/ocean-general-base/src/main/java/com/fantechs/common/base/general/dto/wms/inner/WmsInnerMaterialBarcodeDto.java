package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsInnerMaterialBarcodeDto extends WmsInnerMaterialBarcode implements Serializable {

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQty",value = "订单数量")
    @Transient
    private BigDecimal orderQty;

    /**
     * 打印张数
     */
    @ApiModelProperty(name="printQty",value = "打印张数")
    @Transient
    private int printQty;

    /**
     * 生成数量
     */
    @ApiModelProperty(name="generateQty",value = "生成数量")
    @Transient
    private BigDecimal generateQty;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId" ,value="条码规则集合ID")
    @Transient
    private Long barcodeRuleSetId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Transient
    private String materialName;


    /**
     * 已打印物料总数量
     */
    @ApiModelProperty(name="totalMaterialQty",value = "已打印物料总数量")
    @Transient
    private BigDecimal totalMaterialQty;


    private static final long serialVersionUID = 1L;
}
