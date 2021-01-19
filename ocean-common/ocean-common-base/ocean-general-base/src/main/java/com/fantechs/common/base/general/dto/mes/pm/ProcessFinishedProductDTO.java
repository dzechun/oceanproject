package com.fantechs.common.base.general.dto.mes.pm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/19 17:58
 * @Description: 工序报工
 * @Version: 1.0
 */
@Data
public class ProcessFinishedProductDTO {
    @ApiModelProperty(value = "工单流程卡任务池ID",example = "工单流程卡任务池ID")
    private Long workOrderCardPoolId;
    @ApiModelProperty(value = "工序ID",example = "工序ID")
    private Long processId;
    @ApiModelProperty(value = "本次报工数量",example = "本次报工数量")
    private BigDecimal curOutputQty;
    @ApiModelProperty(value = "操作方式(1、保存 2、提交)",example = "操作方式(1、保存 2、提交)")
    private int operation=1;
}
