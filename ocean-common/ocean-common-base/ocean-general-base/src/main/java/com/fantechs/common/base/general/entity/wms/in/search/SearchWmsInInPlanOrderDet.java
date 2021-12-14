package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsInInPlanOrderDet extends BaseQuery implements Serializable {

    /**
     * 入库计划单ID
     */
    @ApiModelProperty(name="inPlanOrderId",value = "入库计划单ID")
    private Long inPlanOrderId;
}
