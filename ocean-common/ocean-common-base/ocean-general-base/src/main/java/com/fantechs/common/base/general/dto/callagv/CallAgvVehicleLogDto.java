package com.fantechs.common.base.general.dto.callagv;

import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CallAgvVehicleLogDto extends CallAgvVehicleLog implements Serializable {

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "型号")
    private String productModel;

    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;
}
