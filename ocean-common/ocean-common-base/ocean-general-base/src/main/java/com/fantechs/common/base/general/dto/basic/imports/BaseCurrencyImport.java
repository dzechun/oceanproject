package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseCurrencyImport implements Serializable {

    /**
     * 币别编码
     */
    @ApiModelProperty(name="currencyCode" ,value="币别编码")
    @Excel(name = "币别编码(必填)", height = 20, width = 30)
    private String currencyCode;

    /**
     * 币别名称
     */
    @ApiModelProperty(name="currencyName" ,value="币别名称")
    @Excel(name = "币别名称(必填)", height = 20, width = 30)
    private String currencyName;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Integer status;
}
