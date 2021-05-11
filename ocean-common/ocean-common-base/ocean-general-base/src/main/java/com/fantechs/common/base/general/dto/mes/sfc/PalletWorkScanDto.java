package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PalletWorkScanDto {

    @ApiModelProperty(name="palletCode",value = "栈板码")
    private String palletCode;

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

    @ApiModelProperty(name="finishPalletQty",value = "已包栈板数量")
    private BigDecimal finishPalletQty;

    @ApiModelProperty(name="nowPackageSpecQty",value = "当前包装规格数量")
    private BigDecimal nowPackageSpecQty;
}
