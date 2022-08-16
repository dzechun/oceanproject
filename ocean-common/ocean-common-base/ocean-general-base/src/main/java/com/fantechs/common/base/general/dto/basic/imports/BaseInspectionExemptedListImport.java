package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseInspectionExemptedListImport implements Serializable {

    /**
     * 类型(1-供应商 2-客户)(必填)
     */
    @ApiModelProperty(name="objType",value = "类型(1-供应商 2-客户)(必填)")
    @Excel(name = "类型(1-供应商 2-客户)(必填)", height = 20, width = 30)
    private Integer objType;

    /**
     * 供应商编码
     */
    @ApiModelProperty(name = "supplierCode",value = "供应商编码")
    @Excel(name = "供应商编码", height = 20, width = 30)
    private String supplierCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 客户编码
     */
    @ApiModelProperty(name = "customerCode",value = "客户编码")
    @Excel(name = "客户编码", height = 20, width = 30)
    private String customerCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="customerId",value = "客户ID")
    private Long customerId;

    /**
     * 物料编码(必填)
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码(必填)")
    @Excel(name = "物料编码(必填)", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;
}
