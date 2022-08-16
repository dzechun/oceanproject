package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class BaseFactoryImport implements Serializable {

    /**
     * 工厂编码
     */
    @Excel(name = "工厂编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    private String factoryCode;

    /**
     * 工厂名称
     */
    @Excel(name = "工厂名称(必填)", height = 20, width = 30)
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    private String factoryName;

    /**
     * 工厂描述
     */
    @Excel(name = "工厂描述", height = 20, width = 30)
    @ApiModelProperty(name="factoryDesc" ,value="厂别描述")
    private String factoryDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 工厂状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status" ,value="工厂状态（0、不启用 1、启用）")
    @Excel(name = "工厂状态（0、不启用 1、启用）", height = 20, width = 30)
    private Integer status;
}
