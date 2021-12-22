package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsOutPlanDeliveryOrder extends BaseQuery implements Serializable {

    /**
     * 出库计划单ID
     */
    @ApiModelProperty(name="planDeliveryOrderId",value = "出库计划单ID")
    private Long planDeliveryOrderId;

    /**
     * 出库计划单号
     */
    @ApiModelProperty(name="planDeliveryOrderCode",value = "出库计划单号")
    private String planDeliveryOrderCode;

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
