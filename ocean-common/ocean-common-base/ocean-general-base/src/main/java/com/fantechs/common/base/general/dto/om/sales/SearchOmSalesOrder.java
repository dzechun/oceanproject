package com.fantechs.common.base.general.dto.om.sales;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchOmSalesOrder extends BaseQuery implements Serializable {
    @ApiModelProperty(value = "销售订单ID", example = "销售订单ID")
    private Long salesOrderId;
    @ApiModelProperty(value = "客户订单号", example = "客户订单号")
    private String customerOrderCode;
    @ApiModelProperty(value = "合同号", example = "合同号")
    private String contractCode;
}
