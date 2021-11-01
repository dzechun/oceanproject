package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 出库概况
 */
@Data
public class OutboundOverview extends ValidGroup implements Serializable {


    /**
     * 天
     */
    @ApiModelProperty(name="day",value = "天")
    private String day;

    /**
     * 发运量
     */
    @ApiModelProperty(name="shipmentQty",value = "发运量")
    private BigDecimal shipmentQty;

    /**
     * 总量
     */
    @ApiModelProperty(name="total",value = "总量")
    private BigDecimal total;

}
