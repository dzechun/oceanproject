package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchBasePlateParts extends BaseQuery implements Serializable {
    /**
     * 部件组成ID
     */
    @ApiModelProperty(name="platePartsId",value = "部件组成ID")
    private Long platePartsId;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    private String materialName;

    /**
     * 产品型号名称
     */
    @ApiModelProperty(name="productModelName",value = "产品型号名称")
    private String productModelName;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="materialDesc",value = "产品描述")
    private String materialDesc;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId",value = "产品ID")
    private Long materialId;

    /**
     * 是否是定制产品（0、否 1、是）
     */
    @ApiModelProperty(name="ifCustomized",value = "是否是定制产品（0、否 1、是）")
    private Byte ifCustomized;


    private static final long serialVersionUID = 1L;
}
