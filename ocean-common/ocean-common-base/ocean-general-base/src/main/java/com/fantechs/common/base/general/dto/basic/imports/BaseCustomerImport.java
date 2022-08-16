package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseCustomerImport implements Serializable {

    /**
     * 客户代码
     */
    @ApiModelProperty("客户代码")
    @Excel(name = "客户代码(必填)", height = 20, width = 30)
    private String supplierCode;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    @Excel(name = "客户名称(必填)", height = 20, width = 30)
    private String supplierName;

    /**
     * 客户描述
     */
    @ApiModelProperty("客户描述")
    @Excel(name = "客户描述", height = 20, width = 30)
    private String supplierDesc;

    /**
     * 国家名称
     */
    @ApiModelProperty("国家名称")
    @Excel(name = "国家名称", height = 20, width = 30)
    private String countryName;

    /**
     * 大区名称
     */
    @ApiModelProperty("大区名称")
    @Excel(name = "大区名称", height = 20, width = 30)
    private String regionName;

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
    private Integer status;
}
