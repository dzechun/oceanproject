package com.fantechs.common.base.general.dto.mes.pm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/21 10:44
 * @Description: 总计划转流程卡
 * @Version: 1.0
 */
@Data
public class TurnWorkOrderCardPoolDTO {
    @ApiModelProperty(value = "总计划ID",example = "总计划ID")
    private Long masterPlanId;
    @ApiModelProperty(value = "排产数",example = "排产数")
    private BigDecimal scheduleQty;
    @ApiModelProperty(value = "是否生成工单流转卡",example = "是否生成工单流转卡")
    private Boolean generate;
}
