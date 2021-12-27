package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsOutDeliveryReqOrderDet extends BaseQuery implements Serializable {

    /**
     * 出库通知单ID
     */
    @ApiModelProperty(name="deliveryReqOrderId",value = "出库通知单ID")
    private Long deliveryReqOrderId;

}
