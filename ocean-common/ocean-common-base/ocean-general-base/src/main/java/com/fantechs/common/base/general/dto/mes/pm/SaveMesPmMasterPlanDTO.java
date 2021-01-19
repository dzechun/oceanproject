package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/18 10:57
 * @Description: 新增及更新主计划及工序计划
 * @Version: 1.0
 */
@Data
public class SaveMesPmMasterPlanDTO {
    @ApiModelProperty(value = "主计划",example = "主计划")
    private MesPmMasterPlan mesPmMasterPlan;
    @ApiModelProperty(value = "主计划工序计划",example = "主计划工序计划")
    private List<MesPmProcessPlan> mesPmProcessPlanList;
}
