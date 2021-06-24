package com.fantechs.common.base.electronic.dto;

import com.fantechs.common.base.electronic.entity.PtlJobOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PtlJobOrderDetDto extends PtlJobOrderDet implements Serializable {

    @ApiModelProperty(name="jobOrderCode",value = "任务单号")
    private String jobOrderCode;

    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;

    @ApiModelProperty(name="relatedOrderCode",value = "发货单号")
    private String despatchOrderCode;

    @ApiModelProperty(name="equipmentTagId",value = "设备标签ID")
    private String equipmentTagId;

    @ApiModelProperty(name="electronicTagId",value = "电子标签Id")
    private String electronicTagId;

    @ApiModelProperty(name="queueName" ,value="队列名称")
    private String  queueName;

    @ApiModelProperty(name="electronicTagLangType",value = "电子标签语言类别(1-中文 2-英文)")
    private Byte electronicTagLangType;

    @ApiModelProperty(name="equipmentAreaId",value = "区域设备Id")
    private String equipmentAreaId;

    @ApiModelProperty(name="electronicTagId",value = "区域标签Id")
    private String equipmentAreaTagId;

    @ApiModelProperty(name="position",value = "区域设备标签ID顺序位置")
    private String position;

    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域id")
    private Long warehouseAreaId;

    @ApiModelProperty(name = "warehouseAreaCode",value = "仓库区域编码")
    private String warehouseAreaCode;

    @ApiModelProperty(name = "warehouseAreaName",value = "仓库区域名称")
    private String warehouseAreaName;
}
