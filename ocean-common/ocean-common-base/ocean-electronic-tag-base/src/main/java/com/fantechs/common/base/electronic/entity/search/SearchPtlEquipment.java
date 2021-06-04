package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPtlEquipment extends BaseQuery implements Serializable {


    /**
     * 设备编号
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编号")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    private String equipmentName;

    /**
     * 设备ip
     */
    @ApiModelProperty(name="equipmentIp",value = "设备ip")
    private String equipmentIp;

    /**
     * 设备端口
     */
    @ApiModelProperty(name="equipmentPort",value = "设备端口")
    private String equipmentPort;

    /**
     * 设备数据
     */
    @ApiModelProperty(name="equipmentData",value = "设备数据")
    private String equipmentData;

    /**
     * 设备类型(0-控制器 1-区域灯 2-其他)
     */
    @ApiModelProperty(name="equipmentType",value = "设备类型(0-控制器 1-区域灯 2-其他)")
    private Byte equipmentType;

    /**
     * 客户端id
     */
    @ApiModelProperty(name="clientId",value = "客户端id")
    private Long clientId;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "queryMark",value = "查询方式标记")
    private Byte codeQueryMark;
}
