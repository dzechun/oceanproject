package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigStandingBook extends BaseQuery implements Serializable {

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    private String jigBarcodeId;

    /**
     * 财产编码类别(1-固定资产  2-列管品)
     */
    @ApiModelProperty(name="propertyCodeCategory",value = "财产编码类别(1-固定资产  2-列管品)")
    private String propertyCodeCategory;

}
