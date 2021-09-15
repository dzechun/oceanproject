package com.fantechs.common.base.general.dto.eng;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/9/10
 */
@Data
public class EngPackingOrderTakeCancel implements Serializable {

    @ApiModelProperty(name = "packingOrderId",value = "包装清单id")
    private Long packingOrderId;

    @ApiModelProperty(name = "packingOrderSummaryDetId",value = "货品明细id")
    private Long packingOrderSummaryDetId;

    /**
     * 取消数量
     */
    @ApiModelProperty(name = "qty",value = "取消数量")
    private BigDecimal qty;

    private String packingOrderCode;
}
