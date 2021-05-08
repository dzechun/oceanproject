package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesSfcProductCarton extends BaseQuery implements Serializable {

    @ApiModelProperty(name="materialId",value = "产品物料ID")
    private Long materialId;

    @ApiModelProperty(name="barcodeType",value = "条码类别（1.工序流转卡、2.产品条码）")
    private Byte barcodeType;

    @ApiModelProperty(name="cartonCode",value = "包箱号")
    private String cartonCode;

    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;

    @ApiModelProperty(name="barcode",value = "产品条码")
    private String barcode;
}
