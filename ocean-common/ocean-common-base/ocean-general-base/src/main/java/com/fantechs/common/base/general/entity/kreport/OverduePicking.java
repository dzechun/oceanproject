package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 超期拣货单统计
 */
@Data
public class OverduePicking extends ValidGroup implements Serializable {

    /**
     * 近一天
     */
    @ApiModelProperty(name="nearlyOneDay",value = "近一天")
    private BigDecimal nearlyOneDay;

    /**
     * 近三天
     */
    @ApiModelProperty(name="nearlyThreeDay",value = "近三天")
    private BigDecimal nearlyThreeDay;

    /**
     * 近七天
     */
    @ApiModelProperty(name="nearlySevenDay",value = "近七天")
    private BigDecimal nearlySevenDay;

}
