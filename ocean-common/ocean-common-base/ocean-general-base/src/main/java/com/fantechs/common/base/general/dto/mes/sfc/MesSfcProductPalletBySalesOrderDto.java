package com.fantechs.common.base.general.dto.mes.sfc;


import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesSfcProductPalletBySalesOrderDto implements Serializable {

    @ApiModelProperty(name="salesOrderCode",value = "销售订单编码")
    private String salesOrderCode;
}
