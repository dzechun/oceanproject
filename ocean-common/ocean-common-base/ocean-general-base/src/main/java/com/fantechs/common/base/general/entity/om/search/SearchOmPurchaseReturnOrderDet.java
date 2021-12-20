package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/15
 */
@Data
public class SearchOmPurchaseReturnOrderDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "purchaseReturnOrderId",value = "采退订单id")
    private Long purchaseReturnOrderId;
}
