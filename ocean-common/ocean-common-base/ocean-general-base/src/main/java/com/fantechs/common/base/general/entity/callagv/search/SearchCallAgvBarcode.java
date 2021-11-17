package com.fantechs.common.base.general.entity.callagv.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchCallAgvBarcode extends BaseQuery implements Serializable {

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "型号")
    private String productModel;

    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    @ApiModelProperty(name="processName",value = "工序名称")
    private String processName;

    @ApiModelProperty(name="batch",value = "批次")
    private String batch;

    @ApiModelProperty(name="serialNumber",value = "流水号")
    private String serialNumber;

    @ApiModelProperty(name="zhuSuJiName",value = "注塑机名称")
    private String zhuSuJiName;

    @ApiModelProperty(name="zhuSuJiShift",value = "注塑机班次")
    private String zhuSuJiShift;

    @ApiModelProperty(name="zheWanJiName",value = "折弯机名称")
    private String zheWanJiName;

    @ApiModelProperty(name="zheWanJiShift",value = "折弯机班次")
    private String zheWanJiShift;

    @ApiModelProperty(name="materialFactory",value = "材料厂家")
    private String materialFactory;

    @ApiModelProperty(name="materialSpec",value = "材料规格")
    private String materialSpec;

    @ApiModelProperty(name="productionDate",value = "生产日期")
    private String productionDate;

}
