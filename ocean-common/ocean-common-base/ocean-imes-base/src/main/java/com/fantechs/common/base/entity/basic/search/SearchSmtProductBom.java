package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wcz on 2020/10/12.
 */
@Data
public class SearchSmtProductBom extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -6961850550811090030L;

    /**
     * 物料清单编号
     */
    @ApiModelProperty(name="productBomCode" ,value="BOM ID")
    private String productBomCode;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    private Long materialId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;
}
