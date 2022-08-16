package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInspectionExemptedList extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -3896088051789821762L;
    /**
     * 供应商编码
     */
    @ApiModelProperty(name="supplierCode" ,value="供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName" ,value="供应商名称")
    private String supplierName;

    /**
     * 客户编码
     */
    @ApiModelProperty(name="customerCode" ,value="客户编码")
    private String customerCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="customerName" ,value="客户名称")
    private String customerName;

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

}
