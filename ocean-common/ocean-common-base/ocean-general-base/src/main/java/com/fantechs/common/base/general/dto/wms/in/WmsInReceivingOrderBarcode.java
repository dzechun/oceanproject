package com.fantechs.common.base.general.dto.wms.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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
}
