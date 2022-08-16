package com.fantechs.common.base.dto.storage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Mr.Lei
 * @create 2021/3/29
 */

/**
 * 包装管理
 */
@Data
public class ManagerList {

    @ApiModelProperty("包装管理Id")
    private Long packageManagerId;
    @ApiModelProperty("同工单数量叠加")
    private BigDecimal total;
    @ApiModelProperty("同工单数量加1")
    private Integer qty;
}
