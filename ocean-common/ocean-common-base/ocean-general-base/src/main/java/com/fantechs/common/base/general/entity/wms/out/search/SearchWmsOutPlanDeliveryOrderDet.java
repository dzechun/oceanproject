package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsOutPlanDeliveryOrderDet extends BaseQuery implements Serializable {

    /**
     * 出库计划单ID
     */
    @ApiModelProperty(name="planDeliveryOrderId",value = "出库计划单ID")
    private Long planDeliveryOrderId;

}
