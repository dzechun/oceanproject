package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BaseInspectionItemImport implements Serializable {

    /**
     * 检验项目编码-大类(必填)
     */
    @ApiModelProperty(name="inspectionItemCodeBig",value = "检验项目编码-大类(必填)")
    @Excel(name = "检验项目编码-大类(必填)", height = 20, width = 30)
    private String inspectionItemCodeBig;

    /**
     * 检验项目描述-大类(必填)
     */
    @ApiModelProperty(name="inspectionItemDescBig",value = "检验项目描述-大类(必填)")
    @Excel(name = "检验项目描述-大类(必填)", height = 20, width = 30)
    private String inspectionItemDescBig;

    /**
     * 检验项目编码-小类(必填)
     */
    @ApiModelProperty(name="inspectionItemCodeSmall",value = "检验项目编码-小类(必填)")
    @Excel(name = "检验项目编码-小类(必填)", height = 20, width = 30)
    private String inspectionItemCodeSmall;

    /**
     * 检验项目描述-小类(必填)
     */
    @ApiModelProperty(name="inspectionItemDescSmall",value = "检验项目描述-小类(必填)")
    @Excel(name = "检验项目描述-小类(必填)", height = 20, width = 30)
    private String inspectionItemDescSmall;

    /**
     * 检验项目标准
     */
    @ApiModelProperty(name="inspectionItemStandard",value = "检验项目标准")
    @Excel(name = "检验项目标准", height = 20, width = 30)
    private String inspectionItemStandard;

}
