package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class BaseWorkingAreaImport implements Serializable {

    /**
     * 工作区编码(必填)
     */
    @ApiModelProperty(name = "workingAreaCode",value = "工作区编码(必填)")
    @Excel(name = "工作区编码(必填)", height = 20, width = 30)
    private String workingAreaCode;

    /**
     * 所属库区编码(必填)
     */
    @ApiModelProperty(name = "warehouseAreaCode",value = "所属库区编码(必填)")
    @Excel(name = "所属库区编码(必填)", height = 20, width = 30)
    private String warehouseAreaCode;

    /**
     * 所属库区id
     */
    @ApiModelProperty(name = "warehouseAreaCode",value = "所属库区id")
    private Long warehouseAreaId;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

}
