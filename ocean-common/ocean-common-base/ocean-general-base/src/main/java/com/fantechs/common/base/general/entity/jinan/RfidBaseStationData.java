package com.fantechs.common.base.general.entity.jinan;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class RfidBaseStationData implements Serializable {
    /**
     * 基站编码
     */
    @ApiModelProperty(name="baseStationCode",value = "基站编码")
    private String baseStationCode;

    /**
     * RFID信息
     */
    @ApiModelProperty(name="list",value = "RFID信息")
    private List<RfidBaseStationAssetData> list = new ArrayList<>();
}
