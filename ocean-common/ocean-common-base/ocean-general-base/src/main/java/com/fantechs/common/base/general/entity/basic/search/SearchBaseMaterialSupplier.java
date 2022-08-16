package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseMaterialSupplier extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 6411275225883625161L;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
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

    /**
     * 客户料号
     */
    @ApiModelProperty(name="materialSupplierCode",value = "客户料号")
    private String materialSupplierCode;

    /**
     * 客户产品型号
     */
    @ApiModelProperty(name="supplierProductModel",value = "客户产品型号")
    private String supplierProductModel;


    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    private Long supplierId;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="supplierName" ,value="客户名称")
    private String supplierName;
}
