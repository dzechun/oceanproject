package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtMaterialSupplier extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 6411275225883625161L;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 客户料号
     */
    @ApiModelProperty(name="materialSupplierCode",value = "客户料号")
    private String materialSupplierCode;
}
