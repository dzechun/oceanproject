package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsOutDeliveryReqOrder extends BaseQuery implements Serializable {

    /**
     * 出库通知单ID
     */
    @ApiModelProperty(name="deliveryReqOrderId",value = "出库通知单ID")
    private Long deliveryReqOrderId;

    /**
     * 出库通知单号
     */
    @ApiModelProperty(name="deliveryReqOrderCode",value = "出库通知单号")
    private String deliveryReqOrderCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 来源单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源单据类型编码")
    private String sourceSysOrderTypeCode;

    /**
     * 订单状态(1-待执行 2-执行中 3-已执行)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-待执行 2-执行中 3-已执行)")
    private Byte orderStatus;

}
