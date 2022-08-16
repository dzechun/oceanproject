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

    /**
     * 资产条码
     */
    @ApiModelProperty(name="assetCode",value = "资产编码")
    private String assetCode;

    /**
     * 治具编码
     */
    @ApiModelProperty(name="jigCode",value = "治具编码")
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    private String jigName;

}
