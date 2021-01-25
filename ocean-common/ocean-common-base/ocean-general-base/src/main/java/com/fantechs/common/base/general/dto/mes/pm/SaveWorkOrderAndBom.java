package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/11 14:40
 * @Description: 保存工单及工单BOM
 * @Version: 1.0
 */
@Data
public class SaveWorkOrderAndBom {
    @ApiModelProperty(value = "工单对象",example = "工单对象")
    private SmtWorkOrder smtWorkOrder;
    @ApiModelProperty(value = "工单BOM对象集合",example = "工单BOM对象集合")
    private List<SmtWorkOrderBom> smtWorkOrderBomList;
    @ApiModelProperty(value = "是否生成工单流转卡",example = "是否生成工单流转卡")
    private Boolean generate;
}
