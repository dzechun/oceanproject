package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单据数量统计
 */
@Data
public class BillsQtyStatistics extends ValidGroup implements Serializable {

    /**
     * 已收数量
     */
    @ApiModelProperty(name="receivedQty",value = "已收数量")
    private BigDecimal receivedQty;

    /**
     * 未收数量
     */
    @ApiModelProperty(name="unreceivedQty",value = "未收数量")
    private BigDecimal unreceivedQty;

    /**
     * 总数量
     */
    @ApiModelProperty(name="total",value = "总数量")
    private BigDecimal total;

}
