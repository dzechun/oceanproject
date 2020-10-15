package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtProcessCategory extends BaseQuery implements Serializable {

    /**
     * 工序类别代码
     */
    @ApiModelProperty(name="processCategoryCode" ,value="工序类别代码")
    private String processCategoryCode;

    /**
     * 工序类别名称
     */
    @ApiModelProperty(name="processCategoryName" ,value="工序类别名称")
    private String processCategoryName;
}
