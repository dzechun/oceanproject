package com.fantechs.common.base.general.dto.wms.in;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/12/15
 */
@Data
public class WmsInReceivingOrderBarcode implements Serializable {

    @ApiModelProperty(name = "materialBarcodeReOrderId",value = "所有单据条码关系表ID")
    private Long materialBarcodeReOrderId;

    @ApiModelProperty(name = "materialBarcodeId",value = "来料条码ID")
    private Long materialBarcodeId;

    @ApiModelProperty(name = "barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name = "materialQty",value = "物料数量")
    private BigDecimal materialQty;

    @ApiModelProperty(name = "barcodeType",value = "条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)")
    private Byte barcodeType;

    @ApiModelProperty(name = "materialId",value = "物料id")
    private Long materialId;

    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialBarcodeDtoList",value = "条码集合")
    private List<WmsInnerMaterialBarcodeDto> materialBarcodeDtoList;
}
