package com.fantechs.common.base.general.dto.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 工序计划表搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesPmProcessPlanListDTO extends BaseQuery implements Serializable  {
    /**
     * 总计划ID
     */
    @ApiModelProperty(value = "总计划ID",example = "总计划ID")
    @Excel(name = "总计划ID")
    private Long masterPlanId;
}
