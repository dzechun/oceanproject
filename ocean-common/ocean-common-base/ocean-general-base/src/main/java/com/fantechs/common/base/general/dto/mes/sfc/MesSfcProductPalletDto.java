package com.fantechs.common.base.general.dto.mes.sfc;


import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesSfcProductPalletDto  extends MesSfcProductPallet implements Serializable {

    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    private BigDecimal workOrderQty;

    @ApiModelProperty(name="productionQty",value = "投产数量")
    private BigDecimal productionQty;
}
