package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 承运商处理单
 */
@Data
public class CarrierProcessingOrder extends ValidGroup implements Serializable {

    /**
     * 承运商名称
     */
    @ApiModelProperty(name="carrierName",value = "承运商名称")
    private String carrierName;

    /**
     * 发运总量
     */
    @ApiModelProperty(name="total",value = "发运总量")
    private BigDecimal total;

}
