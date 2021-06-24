package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PtlElectronicTagStorageImport implements Serializable {

    @ApiModelProperty(name = "storageId", value = "储位id")
    private String storageId;

    @ApiModelProperty(name = "warehouseId", value = "仓库名称")
    private String warehouseId;

    @ApiModelProperty(name = "warehouseAreaId", value = "仓库区域id")
    private String warehouseAreaId;

    @ApiModelProperty(name = "storageCode", value = "储位编码")
    @Excel(name = "储位编码(必填)", height = 20, width = 30)
    private String storageCode;

    @ApiModelProperty(name = "storageName", value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30)
    private String storageName;

    @ApiModelProperty(name = "equipmentId", value = "设备id（电子标签控制器）")
    private String equipmentId;

    @ApiModelProperty(name = "equipmentCode", value = "电子标签控制器编码")
    @Excel(name = "电子标签控制器编码(必填)", height = 20, width = 30)
    private String equipmentCode;

    @ApiModelProperty(name = "equipmentName", value = "设备名称")
    @Excel(name = "电子标签控制器名称", height = 20, width = 30)
    private String equipmentName;

    @ApiModelProperty(name = "equipmentAreaId", value = "区域设备Id")
    private String equipmentAreaId;

    @ApiModelProperty(name = "equipmentCode", value = "区域设备编码")
    @Excel(name = "区域设备编码(必填)", height = 20, width = 30)
    private String equipmentAreaCode;

    @ApiModelProperty(name = "equipmentName", value = "区域设备名称")
    @Excel(name = "区域设备名称", height = 20, width = 30)
    private String equipmentAreaName;

    @ApiModelProperty(name = "equipmentIp", value = "设备ip")
    @Excel(name = "电子标签控制器ip", height = 20, width = 30)
    private String equipmentIp;

    @ApiModelProperty(name = "equipmentPort", value = "设备端口")
    @Excel(name = "设备端口", height = 20, width = 30)
    private String equipmentPort;

    @ApiModelProperty(name = "electronicTagId", value = "电子标签id")
    @Excel(name = "电子标签id(必填)", height = 20, width = 30)
    private String electronicTagId;

    @ApiModelProperty(name = "electronicTagLangType", value = "电子标签语言类别(1-中文 2-英文)")
    @Excel(name = "电子标签语言类别(1-中文 2-英文)(必填)", height = 20, width = 30)
    private Integer electronicTagLangType;
}
