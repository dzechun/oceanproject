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
public class SearchBaseSafeStock extends BaseQuery implements Serializable {

    /**
     * 仓库Id
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库Id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    /**
     * 货主Id
     */
    @ApiModelProperty(name = "materialOwnerId",value = "货主Id")
    private Long materialOwnerId;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName" ,value="货主名称")
    private String materialOwnerName;

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
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;
}
