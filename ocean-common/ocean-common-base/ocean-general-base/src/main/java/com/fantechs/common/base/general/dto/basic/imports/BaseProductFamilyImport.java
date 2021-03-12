package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class BaseProductFamilyImport implements Serializable {

    /**
     * 产品族编码
     */
    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    @Excel(name = "产品族编码", height = 20, width = 30,orderNum="1")
    @Column(name = "product_family_code")
    @NotBlank(message = "产品族编码不能为空")
    private String productFamilyCode;

    /**
     * 产品族名称
     */
    @ApiModelProperty(name="productFamilyName",value = "产品族名称")
    @Excel(name = "产品族名称", height = 20, width = 30,orderNum="2")
    @Column(name = "product_family_name")
    @NotBlank(message = "产品族名称不能为空")
    private String productFamilyName;

    /**
     * 产品族描述
     */
    @ApiModelProperty(name="productFamilyDesc",value = "产品族描述")
    @Excel(name = "产品族描述", height = 20, width = 30,orderNum="3")
    @Column(name = "product_family_desc")
    private String productFamilyDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30,orderNum="4",replace = {"不启用_0","启用_1"})
    private Byte status;
}
