package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerMaterialBarcodeDto extends WmsInnerMaterialBarcode implements Serializable {

    /**
     * 打印条码数量
     */
    @ApiModelProperty(name="barCodeQty",value = "打印条码数量")
    @Transient
    private int barCodeQty;

    /**
     * 打印张数
     */
    @ApiModelProperty(name="printQty",value = "打印张数")
    @Transient
    private int printQty;

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


    private static final long serialVersionUID = 1L;
}
