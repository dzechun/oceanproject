package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 工单排产表搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesScheduleListDTO extends BaseQuery implements Serializable  {
    @ApiModelProperty(value = "产线ID",example = "产线ID")
    private Long proLineId;
    @ApiModelProperty(value = "排产单号",example = "排产单号")
    private String scheduleCode;
}
