package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 波次管理
 */
@Data
public class WaveManager extends ValidGroup implements Serializable {

    /**
     * 已拣货数量
     */
    @ApiModelProperty(name="pickedGoodsQty",value = "已拣货数量")
    private BigDecimal pickedGoodsQty;

    /**
     * 拣货中数量
     */
    @ApiModelProperty(name="unpickedGoodsQty",value = "拣货数量")
    private BigDecimal unpickedGoodsQty;

    /**
     * 待拣货数量
     */
    @ApiModelProperty(name="waitingPickingQty",value = "待拣货数量")
    private BigDecimal waitingPickingQty;

}
