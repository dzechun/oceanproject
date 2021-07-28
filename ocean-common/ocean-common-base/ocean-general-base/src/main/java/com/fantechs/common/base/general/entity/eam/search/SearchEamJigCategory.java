package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigCategory extends BaseQuery implements Serializable {

    /**
     * 治具类别编码
     */
    @ApiModelProperty(name="jigCategoryCode",value = "治具类别编码")
    private String jigCategoryCode;

    /**
     * 治具类别名称
     */
    @ApiModelProperty(name="jigCategoryName",value = "治具类别名称")
    private String jigCategoryName;
}
