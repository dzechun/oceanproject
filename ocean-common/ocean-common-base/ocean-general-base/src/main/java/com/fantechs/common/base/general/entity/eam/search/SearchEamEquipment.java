package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipment extends BaseQuery implements Serializable {

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    private String equipmentName;

    /**
     * 设备描述
     */
    @ApiModelProperty(name="equipmentDesc",value = "设备描述")
    private String equipmentDesc;

    /**
     * 设备IP
     */
    @ApiModelProperty(name="equipmentIp",value = "设备IP")
    private String equipmentIp;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * MAC地址
     */
    @ApiModelProperty(name="equipmentMacAddress",value = "MAC地址")
    private String equipmentMacAddress;

    /**
     * 设备型号
     */
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    private String equipmentModel;

    /**
     * 设备条码
     */
    @ApiModelProperty(name="equipmentBarcode",value = "设备条码")
    private String equipmentBarcode;
}
