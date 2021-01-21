package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BaseTabDto extends BaseTab implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;


    /**
     * 检验项目单号
     */
    @Transient
    @ApiModelProperty(name="inspectionItemCode",value = "检验项目单号")
    private String inspectionItemCode;

    /**
     * 检验项目名称
     */
    @Transient
    @ApiModelProperty(name="inspectionItemName",value = "检验项目名称")
    private String inspectionItemName;

    /**
     * 检验类型单号
     */
    @Transient
    @ApiModelProperty(name="inspectionTypeCode",value = "检验类型单号")
    private String inspectionTypeCode;

    /**
     * 检验类型名称
     */
    @Transient
    @ApiModelProperty(name="inspectionTypeName",value = "检验类型名称")
    private String inspectionTypeName;

    /**
     * 包装规格编码
     */
    @ApiModelProperty(name="packageSpecificationCode",value = "包装规格编码")
    @Transient
    private String packageSpecificationCode;

    /**
     * 包装规格名称
     */
    @ApiModelProperty(name="packageSpecificationName",value = "包装规格名称")
    @Transient
    private String packageSpecificationName;

    /**
     *  产品型号编码
     */
    @Transient
    @ApiModelProperty(name="productModelCode",value = "产品型号编码")
    private String productModelCode;

    /**
     *  产品型号名称
     */
    @Transient
    @ApiModelProperty(name="productModelCode",value = "产品型号编码")
    private String productModelName;

    /**
     * 标签类别代码
     */
    @ApiModelProperty(name="labelCategoryCode",value = "标签类别代码")
    @Transient
    private String labelCategoryCode;

    /**
     * 标签类别名称
     */
    @ApiModelProperty(name="labelCategoryName",value = "标签类别名称")
    @Transient
    private String labelCategoryName;

    /**
     * 标签代码
     */
    @ApiModelProperty(name="labelCode",value = "标签代码")
    @Transient
    private String labelCode;

    /**
     * 标签名称
     */
    @ApiModelProperty(name="labelName",value = "标签名称")
    @Transient
    private String labelName;

    /**
     * 供应商代码
     */
    @ApiModelProperty("供应商(客户)代码")
    @Transient
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商(客户)名称")
    @Transient
    private String supplierName;
}
