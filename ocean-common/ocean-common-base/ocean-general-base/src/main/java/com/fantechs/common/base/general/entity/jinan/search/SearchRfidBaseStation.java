package com.fantechs.common.base.general.entity.jinan.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRfidBaseStation extends BaseQuery implements Serializable {
    /**
     * 基站编码
     */
    @ApiModelProperty(name="baseStationCode",value = "基站编码")
    private String baseStationCode;

    /**
     * 基站名称
     */
    @ApiModelProperty(name="baseStationName",value = "基站名称")
    private String baseStationName;

    /**
     * 基站描述
     */
    @ApiModelProperty(name="baseStationDesc",value = "基站描述")
    private String baseStationDesc;

    /**
     * 基站型号
     */
    @ApiModelProperty(name="baseStationModel",value = "基站型号")
    private String baseStationModel;

    /**
     * 基站IP
     */
    @ApiModelProperty(name="baseStationIp",value = "基站IP")
    private String baseStationIp;

    /**
     * 基站MAC
     */
    @ApiModelProperty(name="baseStationMac",value = "基站MAC")
    private String baseStationMac;

    /**
     * 区域编码
     */
    @ApiModelProperty(name="areaCode",value = "区域编码")
    private String areaCode;

    /**
     * 区域名称
     */
    @ApiModelProperty(name="areaName",value = "区域名称")
    private String areaName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;
}
