package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseProductModelImport implements Serializable {

    /**
     *  产品型号编码
     */
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Excel(name = "产品型号编码(必填)", height = 20, width = 30)
    private String productModelCode;

    /**
     *  产品型号名称
     */
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    @Excel(name = "产品型号名称(必填)", height = 20, width = 30)
    private String productModelName;

    /**
     *  产品型号描述
     */
    @ApiModelProperty(name="productModelDesc" ,value="产品型号描述")
    @Excel(name = "产品型号描述", height = 20, width = 30)
    private String productModelDesc;

    /**
     * 产品族id
     */
    @ApiModelProperty(name="productFamilyId",value = "产品族id")
    private Long productFamilyId;

    /**
     * 产品族编码
     */
    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    @Excel(name = "产品族编码", height = 20, width = 30)
    private String productFamilyCode;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30)
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
