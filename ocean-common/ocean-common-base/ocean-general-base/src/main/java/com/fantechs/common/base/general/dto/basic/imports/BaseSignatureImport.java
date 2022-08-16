package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseSignatureImport implements Serializable {

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码(必填)", height = 20, width = 30)
    private String materialCode;

    /**
     * 特征码
     */
    @ApiModelProperty(name="signatureCode" ,value="特征码")
    @Excel(name = "特征码(必填)", height = 20, width = 30)
    private String signatureCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId" ,value="供应商ID")
    private Long supplierId;

    /**
     * 供应商编码
     */
    @ApiModelProperty(name="supplierCode" ,value="供应商编码")
    @Excel(name = "供应商编码", height = 20, width = 30)
    private String supplierCode;

    /**
     * 备注
     */
    @Excel(name = "备注", height = 20, width = 30)
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Integer status;
}
