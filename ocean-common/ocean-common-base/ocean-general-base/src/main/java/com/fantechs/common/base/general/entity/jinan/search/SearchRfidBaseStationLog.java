package com.fantechs.common.base.general.entity.jinan.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRfidBaseStationLog extends BaseQuery implements Serializable {
    /**
     * 区域名称
     */
    @ApiModelProperty(name="areaName",value = "区域名称")
    private String areaName;

    /**
     * 区域编码
     */
    @ApiModelProperty(name="areaCode",value = "区域编码")
    private String areaCode;

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
     * 正常信息
     */
    @ApiModelProperty(name="normalInfo",value = "正常信息")
    private String normalInfo;

    /**
     * 不正常信息
     */
    @ApiModelProperty(name="abnormalInfo",value = "不正常信息")
    private String abnormalInfo;

    /**
     * 是否正常(0-否 1-是)
     */
    @ApiModelProperty(name="ifNormal",value = "是否正常(0-否 1-是)")
    private Integer ifNormal;


}
