package com.fantechs.common.base.general.entity.jinan.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRfidBaseStationReAsset extends BaseQuery implements Serializable {

    /**
     * 基站ID
     */
    @ApiModelProperty(name="baseStationId",value = "基站ID")
    private Long baseStationId;

    /**
     * 资产ID
     */
    @ApiModelProperty(name="assetId",value = "资产ID")
    private Long assetId;
}
