package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GoodsDetail {

    @ApiModelProperty(name = "batchNumber", value = "生产批次")
    private String batchNumber;

    @ApiModelProperty(name = "materialCodes", value = "物料编码")
    private String materialCodes;

    @ApiModelProperty(name = "actualQuantity", value = "数量")
    private Double actualQuantity;

    @ApiModelProperty(name = "barCode", value = "条码")
    private String barCode;
}
