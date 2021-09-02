package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEngPackingOrderSummary extends BaseQuery implements Serializable {

    /**
     * 装箱单ID
     */
    @ApiModelProperty(name="packingOrderId",value = "装箱单ID")
    private Long packingOrderId;
}
