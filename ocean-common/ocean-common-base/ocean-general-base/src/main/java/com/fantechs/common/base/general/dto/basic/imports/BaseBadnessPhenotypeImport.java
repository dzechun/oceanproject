package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseBadnessPhenotypeImport implements Serializable {

    /**
     * 不良现象编码(必填)
     */
    @ApiModelProperty(name="badnessPhenotypeCode",value = "不良现象编码(必填)")
    @Excel(name = "不良现象编码(必填)", height = 20, width = 30)
    private String badnessPhenotypeCode;

    /**
     * 不良现象描述
     */
    @ApiModelProperty(name="badnessPhenotypeDesc",value = "不良现象描述")
    @Excel(name = "不良现象描述", height = 20, width = 30)
    private String badnessPhenotypeDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
