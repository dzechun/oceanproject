package com.fantechs.provider.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/12/10.
 */
@Data
public class PtlSortingDetailDTO implements Serializable{

    @ApiModelProperty(name="locationCode",value = "库位编码")
    private String locationCode;

    @ApiModelProperty(name="goodsCode",value = "物料编码")
    private String goodsCode;

    @ApiModelProperty(name="qty",value = "包装数量")
    private Double qty;

    @ApiModelProperty(name="unit",value = "包装单位")
    private String unit;
}
