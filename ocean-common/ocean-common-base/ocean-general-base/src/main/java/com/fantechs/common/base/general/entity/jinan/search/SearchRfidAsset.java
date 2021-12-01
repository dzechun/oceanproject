package com.fantechs.common.base.general.entity.jinan.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRfidAsset extends BaseQuery implements Serializable {
    /**
     * 资产编码
     */
    @ApiModelProperty(name="assetCode",value = "资产编码")
    private String assetCode;

    /**
     * 资产名称
     */
    @ApiModelProperty(name="assetName",value = "资产名称")
    private String assetName;

    /**
     * 资产描述
     */
    @ApiModelProperty(name="assetDesc",value = "资产描述")
    private String assetDesc;

    /**
     * 资产条码
     */
    @ApiModelProperty(name="assetBarcode",value = "资产条码")
    private String assetBarcode;
}
