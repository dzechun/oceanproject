package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseWorkshopSectionImport implements Serializable {

    /**
     * 工段代码
     */
    @ApiModelProperty("工段代码")
    @Excel(name = "工段代码(必填)", height = 20, width = 30)
    private String sectionCode;

    /**
     * 工段名称
     */
    @ApiModelProperty("工段名称")
    @Excel(name = "工段名称(必填)", height = 20, width = 30)
    private String sectionName;

    /**
     * 工段描述
     */
    @ApiModelProperty("工段描述")
    @Excel(name = "工段描述", height = 20, width = 30)
    private String sectionDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty("状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Integer status;
}
