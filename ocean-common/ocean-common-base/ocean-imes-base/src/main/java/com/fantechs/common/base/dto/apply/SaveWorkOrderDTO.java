package com.fantechs.common.base.dto.apply;

import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/8 10:54
 * @Description: 新增或更新工单及工单BOM
 * @Version: 1.0
 */
@Data
public class SaveWorkOrderDTO {
    @ApiModelProperty(value = "工单信息",example = "工单信息")
    private SmtWorkOrder smtWorkOrder;
    @ApiModelProperty(value = "工单BOM信息",example = "工单BOM信息")
    private List<SmtWorkOrderBom> smtWorkOrderBomList;
}
