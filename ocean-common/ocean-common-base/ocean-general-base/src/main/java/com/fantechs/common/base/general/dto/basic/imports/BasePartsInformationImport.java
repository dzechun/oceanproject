package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BasePartsInformationImport implements Serializable {

    /**
     * 部件资料编码
     */
    @ApiModelProperty(name="partsInformationCode",value = "部件编码")
    @Excel(name = "部件资料编码(必填)", height = 20, width = 30)
    private String partsInformationCode;

    /**
     * 部件资料名称
     */
    @ApiModelProperty(name="partsInformationName",value = "部件名称")
    @Excel(name = "部件资料名称(必填)", height = 20, width = 30)
    private String partsInformationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30)
    private Byte status;

}
