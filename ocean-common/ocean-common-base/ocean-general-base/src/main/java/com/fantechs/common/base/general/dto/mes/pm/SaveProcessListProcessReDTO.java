package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/19 20:42
 * @Description: 流程单工序报工退回
 * @Version: 1.0
 */
@Data
public class SaveProcessListProcessReDTO {
    @ApiModelProperty(value = "操作方式（1、保存 2、提交）",example = "操作方式（1、保存 2、提交）")
    private Byte operation=1;

    @ApiModelProperty(value = "工单流程卡任务池ID",example = "工单流程卡任务池ID")
    private Long workOrderCardPoolId;

    @ApiModelProperty(value = "操作退回工序ID",example = "操作退回工序ID")
    private Long processId;

    @ApiModelProperty(value = "指定退回工序ID",example = "指定退回工序ID")
    private Long reProcessId;

    @ApiModelProperty(value = "工艺路线ID",example = "工艺路线ID")
    private Long routeId;

    @ApiModelProperty(value = "员工id",example = "员工id")
    private Long staffId;

    @ApiModelProperty(value = "退回数量",example = "退回数量")
    private java.math.BigDecimal reQty;

    @ApiModelProperty(value = "上次报工数量",example = "上次报工数量")
    private java.math.BigDecimal preQty;
}
