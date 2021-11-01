package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 人员拣货排名统计
 */
@Data
public class PersonnelPickingRanking extends ValidGroup implements Serializable {

    /**
     * 用户名称
     */
    @ApiModelProperty(name="name",value = "用户名称")
    private String name;

    /**
     * 用户UNIKEY
     */
    @ApiModelProperty(name="unikey",value = "用户UNIKEY")
    private String unikey;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;

    /**
     * 上架数量
     */
    @ApiModelProperty(name="putawayQty",value = "上架数量")
    private BigDecimal putawayQty;

    /**
     * 拣货数量
     */
    @ApiModelProperty(name="pickQty",value = "拣货数量")
    private BigDecimal pickQty;

    /**
     * 复核数量
     */
    @ApiModelProperty(name="recheckQty",value = "复核数量")
    private BigDecimal recheckQty;

    /**
     * 补货数量
     */
    @ApiModelProperty(name="replenishmentQty",value = "补货数量")
    private BigDecimal replenishmentQty;

}
