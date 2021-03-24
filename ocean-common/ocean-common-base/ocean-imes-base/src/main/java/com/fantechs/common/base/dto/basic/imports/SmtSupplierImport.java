package com.fantechs.common.base.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SmtSupplierImport implements Serializable {

    /**
     * 供应商代码
     */
    @ApiModelProperty("供应商(客户)代码")
    @Excel(name = "供应商/客户代码(必填)", height = 20, width = 30)
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商(客户)名称")
    @Excel(name = "供应商/客户名称(必填)", height = 20, width = 30)
    private String supplierName;

    /**
     * 供应商描述
     */
    @ApiModelProperty("供应商(客户)描述")
    @Excel(name = "供应商/客户描述", height = 20, width = 30)
    private String supplierDesc;

    /**
     * 身份标识（1、供应商 2、客户）
     */
    @ApiModelProperty("身份标识（1、供应商 2、客户）")
    @Excel(name = "身份标识（1、供应商 2、客户）（必填）", height = 20, width = 30)
    private Byte supplierType;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty("状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Byte status;
}
