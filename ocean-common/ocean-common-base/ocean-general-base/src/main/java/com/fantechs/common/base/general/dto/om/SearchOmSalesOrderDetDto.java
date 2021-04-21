package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchOmSalesOrderDetDto extends BaseQuery implements Serializable {
    /**
     * 销售订单ID
     */
    @ApiModelProperty(name="salesOrderId",value = "销售订单ID")
    private Long salesOrderId;
}
