package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseProcessCategoryImport implements Serializable {

    /**
     * 工序类别编码
     */
    @ApiModelProperty(name="processCategoryCode" ,value="工序类别编码")
    @Excel(name = "工序类别编码(必填)", height = 20, width = 30)
    private String processCategoryCode;

    /**
     * 工序类别名称
     */
    @ApiModelProperty(name="processCategoryName" ,value="工序类别名称")
    @Excel(name = "工序类别名称(必填)", height = 20, width = 30)
    private String processCategoryName;

    /**
     * 工序类别描述
     */
    @ApiModelProperty(name="processCategoryDesc" ,value="工序类别描述")
    @Excel(name = "工序类别描述", height = 20, width = 30)
    private String processCategoryDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Integer status;
}
