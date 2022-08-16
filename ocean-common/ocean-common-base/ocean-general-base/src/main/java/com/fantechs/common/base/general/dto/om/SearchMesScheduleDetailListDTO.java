package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 排产详情搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesScheduleDetailListDTO extends BaseQuery implements Serializable  {
    @ApiModelProperty(value = "排产单ID",example = "排产单ID")
    private String scheduleId;
}
