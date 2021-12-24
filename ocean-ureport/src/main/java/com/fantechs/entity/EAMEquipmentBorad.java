package com.fantechs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EAMEquipmentBorad {


    @ApiModelProperty(name = "equipmentName",value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(name = "equipmentId",value = "设备Id")
    private String equipmentId;

    @ApiModelProperty(name = "equipmentCode",value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(name = "equipmentBarCode",value = "设备条码")
    private String equipmentBarCode;

    @ApiModelProperty(name = "runingTime",value = "运行时间")
    private String runingTime;

    @ApiModelProperty(name = "productNum",value = "生产数量")
    private int productNum;

    @ApiModelProperty(name = "ngNum",value = "ng数量")
    private int ngNum;




}
