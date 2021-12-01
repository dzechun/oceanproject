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
     * 基站名称
     */
    @ApiModelProperty(name="baseStationName",value = "基站名称")
    private String baseStationName;

    /**
     * 读取结果(0-失败 1-成功)
     */
    @ApiModelProperty(name="readResult",value = "读取结果(0-失败 1-成功)")
    private Byte readResult;

    /**
     * RFID
     */
    @ApiModelProperty(name="assetBarcode",value = "RFID")
    private String assetBarcode;

    /**
     * 资产名称
     */
    @ApiModelProperty(name="assetName",value = "资产名称")
    private String assetName;
}
