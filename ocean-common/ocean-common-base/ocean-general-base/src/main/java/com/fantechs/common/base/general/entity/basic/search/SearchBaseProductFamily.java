package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseProductFamily extends BaseQuery implements Serializable {

    /**
     * 产品族编码
     */
    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    private String productFamilyCode;

    /**
     * 产品族名称
     */
    @ApiModelProperty(name="productFamilyName",value = "产品族名称")
    private String productFamilyName;

    /**
     * 产品族描述
     */
    @ApiModelProperty(name="productFamilyDesc",value = "产品族描述")
    private String productFamilyDesc;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    private Byte status;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "queryMark",value = "查询方式标记")
    private Byte codeQueryMark;
}
