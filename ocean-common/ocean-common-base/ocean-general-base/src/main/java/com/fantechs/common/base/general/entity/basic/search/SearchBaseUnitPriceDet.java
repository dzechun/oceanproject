package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseUnitPriceDet extends BaseQuery implements Serializable {

    /**
     * 单价信息ID
     */
    @ApiModelProperty(name="unitPriceId",value = "单价信息ID")
    private Long unitPriceId;
}
