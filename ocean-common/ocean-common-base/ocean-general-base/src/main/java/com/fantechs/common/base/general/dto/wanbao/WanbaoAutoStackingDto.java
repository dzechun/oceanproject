package com.fantechs.common.base.general.dto.wanbao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WanbaoAutoStackingDto implements Serializable {

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @ApiModelProperty(name="stackingId",value = "堆码号ID")
    private Long stackingId;

    @ApiModelProperty(name="stackingCode",value = "堆垛编码")
    private String stackingCode;

    @ApiModelProperty(name="barcode",value = "成品条码")
    private String barcode;

    @ApiModelProperty(name="customerBarcode",value = "客户条码")
    private String customerBarcode;

    @ApiModelProperty(name="salesBarcode",value = "销售条码")
    private String salesBarcode;

    @ApiModelProperty(name="stackingDetId",value = "堆垛条码明细ID")
    private Long stackingDetId;

}
