package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseLabelVariable extends BaseQuery implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 标签类别编码
     */
    @ApiModelProperty(name="labelCategoryCode",value = "标签类别编码")
    private String labelCategoryCode;

    /**
     * 标签类别名称
     */
    @ApiModelProperty(name="labelCategoryName",value = "标签类别名称")
    private String labelCategoryName;

}
