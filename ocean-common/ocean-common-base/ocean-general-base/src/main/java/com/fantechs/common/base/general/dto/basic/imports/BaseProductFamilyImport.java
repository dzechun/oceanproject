package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;

@Data
public class BaseProductFamilyImport implements Serializable {

    /**
     * 产品族编码
     */
    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    @Excel(name = "产品族编码(必填)", height = 20, width = 30)
    private String productFamilyCode;

    /**
     * 产品族名称
     */
    @ApiModelProperty(name="productFamilyName",value = "产品族名称")
    @Excel(name = "产品族名称(必填)", height = 20, width = 30)
    private String productFamilyName;

    /**
     * 产品族描述
     */
    @ApiModelProperty(name="productFamilyDesc",value = "产品族描述")
    @Excel(name = "产品族描述", height = 20, width = 30)
    private String productFamilyDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30)
    private Integer status;
}
