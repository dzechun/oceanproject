package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@Data
public class InitStockCheckBarCode implements Serializable {

    @ApiModelProperty(name = "barCode",value = "扫码的条码")
    private String barCode;

    @ApiModelProperty(name = "type",value = "扫码条码类型（1-厂内码 2-销售条码 3-三星客户条码(带厂内码及物料信息) 4-客户条码）")
    private Byte type;

    @ApiModelProperty(name = "inPlantBarcode",value = "厂内码")
    private String inPlantBarcode;

    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String salesBarcode;

    @ApiModelProperty(name = "clientBarcode",value = "客户条码")
    private String clientBarcode;

    @ApiModelProperty(name = "materialId",value = "物料id")
    private Long materialId;

    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;
}
