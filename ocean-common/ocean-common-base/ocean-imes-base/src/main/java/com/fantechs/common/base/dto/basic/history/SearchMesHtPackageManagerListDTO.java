package com.fantechs.common.base.dto.basic.history;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 包装管理履历表搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesHtPackageManagerListDTO extends BaseQuery implements Serializable  {
    @ApiModelProperty(value = "查询预留字段",example = "查询预留字段")
    private String str;
}
