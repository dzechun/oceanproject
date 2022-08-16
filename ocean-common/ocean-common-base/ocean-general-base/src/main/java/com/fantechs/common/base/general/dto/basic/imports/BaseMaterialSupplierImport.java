package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseMaterialSupplierImport implements Serializable {

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码(必填)", height = 20, width = 3)
    private String materialCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    private Long supplierId;

    /**
     * 客户编码
     */
    @ApiModelProperty(name="supplierCode",value = "客户编码")
    @Excel(name = "客户编码(必填)", height = 20, width = 3)
    private String supplierCode;

    /**
     * 客户料号
     */
    @ApiModelProperty(name="materialSupplierCode",value = "客户料号")
    @Excel(name = "客户料号(必填)", height = 20, width = 30)
    private String materialSupplierCode;

    /**
     * 图片
     */
    @ApiModelProperty(name="image",value = "图片")
    @Excel(name = "图片", height = 20, width = 30)
    private String image;

    /**
     * 客户产品型号
     */
    @ApiModelProperty(name="supplierProductModel",value = "客户产品型号")
    @Excel(name = "客户产品型号", height = 20, width = 30)
    private String supplierProductModel;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Integer status;
}
