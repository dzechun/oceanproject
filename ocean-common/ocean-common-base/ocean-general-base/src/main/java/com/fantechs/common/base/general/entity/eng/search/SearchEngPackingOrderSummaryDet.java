package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEngPackingOrderSummaryDet extends BaseQuery implements Serializable {

    /**
     * 装箱汇总ID
     */
    @ApiModelProperty(name="packingOrderSummaryId",value = "装箱汇总ID")
    private Long packingOrderSummaryId;

}
