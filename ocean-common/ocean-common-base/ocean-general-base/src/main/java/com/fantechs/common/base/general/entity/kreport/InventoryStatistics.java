package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存数量统计
 */
@Data
public class InventoryStatistics extends ValidGroup implements Serializable {

    /**
     * 产品名称
     */
    @ApiModelProperty(name="name",value = "产品名称")
    private String name;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;

    /**
     * 总数量
     */
    @ApiModelProperty(name="total",value = "总数量")
    private BigDecimal total;

}
