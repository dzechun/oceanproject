package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchOmSalesOrderDetDto extends BaseQuery implements Serializable {
    /**
     * 销售订单ID
     */
    @ApiModelProperty(name="salesOrderId",value = "销售订单ID")
    private Long salesOrderId;

    @ApiModelProperty(name="salesOrderDetId",value = "销售订单明细ID")
    private Long salesOrderDetId;

    @ApiModelProperty(name = "materialCode", value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name="salesOrderCode",value = "销售订单编码")
    private String salesOrderCode;

    @ApiModelProperty(name="salesCode",value = "销售编码")
    private String salesCode;

    /**
     * 销售订单ID列表
     */
    @ApiModelProperty(name="salesOrderIdList",value = "销售订单ID列表")
    private List<Long> salesOrderIdList;

    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    private Integer ifAllIssued;
}
