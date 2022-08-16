package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MesSfcPalletReportDto implements Serializable {

    @ApiModelProperty(name="palletCode",value = "栈板号")
    private String palletCode;

    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;

    @ApiModelProperty(name="nowPackageSpecQty",value = "当前包装规格数量")
    private BigDecimal nowPackageSpecQty;

    @ApiModelProperty(name="scanCartonNum",value = "扫描包箱数")
    private BigDecimal scanCartonNum;
}
