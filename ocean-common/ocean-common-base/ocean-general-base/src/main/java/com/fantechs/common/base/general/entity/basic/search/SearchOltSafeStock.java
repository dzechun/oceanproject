package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/3/4
 */
@Data
public class SearchOltSafeStock extends BaseQuery implements Serializable {

    /**
     * 仓库Id
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库名称")
    private Long warehouseId;

    /**
     * 物料类别Id
     */
    @ApiModelProperty(name="materialCategoryId",value = "物料类别Id")
    private Long materialCategoryId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    private Long materialId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;
}
