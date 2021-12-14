package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/13
 */
@Data
public class SearchWmsInPlanReceivingOrderDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "planReceivingOrderId",value = "计划单id")
    private Long planReceivingOrderId;
}
