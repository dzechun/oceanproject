package com.fantechs.provider.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/12/10.
 */
@Data
public class PtlJobOrderDetDTO implements Serializable{

    @ApiModelProperty(name="locationCode",value = "库位编码")
    private String locationCode;

    @ApiModelProperty(name="goodsCode",value = "物料编码")
    private String goodsCode;

    @ApiModelProperty(name="w_qty",value = "整件数量")
    private Double w_qty;

    @ApiModelProperty(name="w_unit",value = "整件单位")
    private String w_unit;

    @ApiModelProperty(name="w_qty",value = "散件数量")
    private Double l_qty;

    @ApiModelProperty(name="w_unit",value = "散件单位")
    private String l_unit;

    @ApiModelProperty(name="specification",value = "规格")
    private String specification;
}
