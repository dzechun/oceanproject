package com.fantechs.common.base.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class SmtProductModelImport implements Serializable {

    /**
     *  产品型号编码
     */
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    private String productModelCode;

    /**
     *  产品型号名称
     */
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    private String productModelName;

    /**
     *  产品型号描述
     */
    @ApiModelProperty(name="productModelDesc" ,value="产品型号描述")
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
    private String productFamilyCode;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    private Long organizationId;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode",value = "组织名称")
    private String organizationCode;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;
}
