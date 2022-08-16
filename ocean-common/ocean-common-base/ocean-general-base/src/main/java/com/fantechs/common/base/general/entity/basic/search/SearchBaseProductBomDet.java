package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchBaseProductBomDet extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -3380145776593750438L;

    /**
     * 产品BOM ID
     */
    @ApiModelProperty(name="productBomId" ,value="产品BOM ID")
    private Long productBomId;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    private Long materialId;

    /**
     * 零件料号
     */
    @ApiModelProperty(name="materialCode" ,value="零件料号")
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 产品BOM详细ID
     */
    @ApiModelProperty(name="productBomDetId" ,value="产品BOM详细ID")
    private Long productBomDetId;

}
