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
public class SearchOmPurchaseOrderDet extends BaseQuery implements Serializable {

    /**
     * 采购订单明细ID
     */
    @ApiModelProperty(name="purchaseOrderDetId",value = "采购订单明细ID")
    private Long purchaseOrderDetId;

}
