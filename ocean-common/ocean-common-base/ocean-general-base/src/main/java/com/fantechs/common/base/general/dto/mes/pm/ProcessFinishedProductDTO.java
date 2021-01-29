package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

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
    private Byte operation=1;
    @ApiModelProperty(value = "工序操作类型（1、开工 2、报工）",example = "工序操作类型（1、开工 2、报工）")
    private Byte processType;
    @ApiModelProperty(value = "开料（0、否 1、是）",example = "开料（0、否 1、是）")
    private Integer putInto=0;
    @ApiModelProperty(value = "部件组成ID集合",example = "部件组成ID集合")
    private List<Long> platePartsDetIdList=new LinkedList<>();
}
