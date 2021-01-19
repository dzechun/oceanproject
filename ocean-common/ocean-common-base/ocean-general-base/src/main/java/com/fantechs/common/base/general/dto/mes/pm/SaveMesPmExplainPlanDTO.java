package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainProcessPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/18 10:57
 * @Description: 新增及更新执行计划及工序计划
 * @Version: 1.0
 */
@Data
public class SaveMesPmExplainPlanDTO {
    @ApiModelProperty(value = "执行计划",example = "执行计划")
    private MesPmExplainPlan mesPmExplainPlan;
    @ApiModelProperty(value = "执行计划工序计划",example = "执行计划工序计划")
    private List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList;
}
