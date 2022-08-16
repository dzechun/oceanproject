package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ScanByManualOperationDto {

    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    @ApiModelProperty(name="proName",value = "产线名称")
    private String proName;

    @ApiModelProperty(name="barcode" ,value="厂内码")
    private String barcode;

    @ApiModelProperty(name="salesBarcode" ,value="销售条码")
    private String salesBarcode;

    @ApiModelProperty(name="customerBarcode" ,value="客户条码")
    private String customerBarcode;

}
