package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/18 13:50
 * @Description: 转执行计划
 * @Version: 1.0
 */
@Data
public class TurnExplainPlanDTO {
    @ApiModelProperty(value = "总计划ID",example = "总计划ID")
    private Long masterPlanId;

    @ApiModelProperty(value = "本次排产数量",example = "本次排产数量")
    private BigDecimal theScheduleQty;

    @ApiModelProperty(value = "计划开工时间",example = "计划开工时间")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    private Date planedStartDate;

    @ApiModelProperty(value = "计划完工时间",example = "计划完工时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    private Date planedEndDate;
}
