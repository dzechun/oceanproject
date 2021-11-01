package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存占比
 */
@Data
public class InventoryProportion extends ValidGroup implements Serializable {

    /**
     * 产品编码
     */
    @ApiModelProperty(name="code",value = "产品编码")
    private String code;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;

    /**
     * 库存总数量
     */
    @ApiModelProperty(name="totalQty",value = "库存总数量")
    private BigDecimal totalQty;

    /**
     * 占比
     */
    @ApiModelProperty(name="receivedRatio",value = "占比")
    private BigDecimal receivedRatio;


}
