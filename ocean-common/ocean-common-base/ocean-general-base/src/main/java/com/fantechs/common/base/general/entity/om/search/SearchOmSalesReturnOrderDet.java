package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/21
 */
@Data
public class SearchOmSalesReturnOrderDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "salesReturnOrderId",value = "销退订单id")
    private Long salesReturnOrderId;

    @ApiModelProperty(name = "salesReturnOrderDetId",value = "销退订单明细id")
    private Long salesReturnOrderDetId;

    @ApiModelProperty(name = "salesReturnOrderCode",value = "销退单号")
    private String salesReturnOrderCode;

    /**
     * 销售订单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售订单号")
    private String salesOrderCode;

    /**
     * 销售订单ID
     */
    @ApiModelProperty(name="salesOrderId",value = "销售订单ID")
    private Long salesOrderId;
}
