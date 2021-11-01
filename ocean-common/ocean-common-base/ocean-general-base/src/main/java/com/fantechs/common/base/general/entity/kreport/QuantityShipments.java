package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存数量折线图统计
 */
@Data
public class QuantityShipments extends ValidGroup implements Serializable {

    /**
     * x轴
     */
    @ApiModelProperty(name="x",value = "x轴")
    private String x;

    /**
     * y轴
     */
    @ApiModelProperty(name="y",value = "y轴")
    private String y;

}
