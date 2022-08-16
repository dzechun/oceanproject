package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamSparePartCategory extends BaseQuery implements Serializable {
    /**
     * 备用件类别编码
     */
    @ApiModelProperty(name="sparePartCategoryCode",value = "备用件类别编码")
    private String sparePartCategoryCode;

    /**
     * 备用件类别名稱
     */
    @ApiModelProperty(name="sparePartCategoryName",value = "备用件类别名稱")
    private String sparePartCategoryName;

    /**
     * 备用件类别描述
     */
    @ApiModelProperty(name="sparePartCategoryDesc",value = "备用件类别描述")
    private String sparePartCategoryDesc;

}
