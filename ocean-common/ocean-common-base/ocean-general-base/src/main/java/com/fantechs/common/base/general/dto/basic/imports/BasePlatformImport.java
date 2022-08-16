package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class BasePlatformImport implements Serializable {

    /**
     * 月台编码
     */
    @Excel(name = "月台编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="platformCode" ,value="月台编码")
    private String platformCode;

    /**
     * 月台名称
     */
    @Excel(name = "月台名称(必填)", height = 20, width = 30)
    @ApiModelProperty(name="platformName" ,value="月台名称")
    private String platformName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

}
