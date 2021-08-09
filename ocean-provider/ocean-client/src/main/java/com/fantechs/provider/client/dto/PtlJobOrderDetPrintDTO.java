package com.fantechs.provider.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PtlJobOrderDetPrintDTO {

    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    private Long jobOrderId;

    @ApiModelProperty(name="jobOrderCode",value = "拣货单号")
    private String jobOrderCode;

    @ApiModelProperty(name="despatchOrderCode",value = "灯颜色")
    private String despatchOrderCode;

    @ApiModelProperty(name="relatedOrderCode",value = "拣货单号")
    private String relatedOrderCode;

    @ApiModelProperty(name="materialName",value = "商品名称")
    private String materialName;

    @ApiModelProperty(name="materialCode",value = "商品编号")
    private String materialCode;

    @ApiModelProperty(name="spec",value = "规格")
    private String spec;

    @ApiModelProperty(name = "warehouseAreaCode",value = "库区")
    private String warehouseAreaCode;

    @ApiModelProperty(name="storageCode",value = "库位")
    private String storageCode;

    @ApiModelProperty(name="workerUserName",value = "拣货人员")
    private String workerUserName;

    @ApiModelProperty(name="cartonCode",value = "整件箱号")
    private String cartonCode;

    @ApiModelProperty(name="vehicleCode",value = "集货号")
    private String vehicleCode;

    @ApiModelProperty(name="wholeOrScattered",value = "整或者零(0-零 1-整)")
    private Byte wholeOrScattered;

    @ApiModelProperty(name="queueName" ,value="队列名称")
    private String  queueName;
}
