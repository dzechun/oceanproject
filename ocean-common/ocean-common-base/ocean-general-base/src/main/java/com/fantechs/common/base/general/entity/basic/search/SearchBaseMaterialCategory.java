package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SearchBaseMaterialCategory extends BaseQuery implements Serializable {

    /**
     * ID
     */
    @ApiModelProperty(name="materialCategoryId",value = "物料类别ID")
    private Long materialCategoryId;

    /**
     * 物料类别编码
     */
    @ApiModelProperty(name="materialCategoryCode",value = "物料类别编码")
    private String materialCategoryCode;

    /**
     * 物料类别名称
     */
    @ApiModelProperty(name="materialCategoryName",value = "物料类别名称")
    private String materialCategoryName;

    /**
     * 物料类别描述
     */
    @ApiModelProperty(name="materialCategoryDesc",value = "物料类别描述")
    private String materialCategoryDesc;

    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    private Long parentId;


    private static final long serialVersionUID = 1L;
}
