package com.fantechs.common.base.general.dto.wms.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */
@Data
public class BarPODto implements Serializable {

    @ApiModelProperty(name = "barCode",value = "订单条码")
    private String barCode;

    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String salesBarcode;

    @ApiModelProperty(name = "cutsomerBarcode",value = "客户条码")
    private String cutsomerBarcode;

    @ApiModelProperty(name = "POCode",value = "PO号")
    private String POCode;
}
