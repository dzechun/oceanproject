package com.fantechs.common.base.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SmtMaterialDto extends SmtMaterial implements Serializable {

    /**
     *  产品型号编码
     */
    @Transient
    @ApiModelProperty(name="productModelCode" ,value="产品型号")
    @Excel(name = "产品型号", height = 20, width = 30)
    private String productModelCode;

    /**
     * 产品族编码
     */
    @Transient
    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    @Excel(name = "产品族编码", height = 20, width = 30)
    private String productFamilyCode;

    /**
     * 产品族名称
     */
    @Transient
    @ApiModelProperty(name="productFamilyName",value = "产品族名称")
    @Excel(name = "产品族名称", height = 20, width = 30)
    private String productFamilyName;

    /**
     * 产品族描述
     */
    @Transient
    @ApiModelProperty(name="productFamilyDesc",value = "产品族描述")
    @Excel(name = "产品族描述", height = 20, width = 30)
    private String productFamilyDesc;

    /**
     * 条码规则集合名称
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleName" ,value="条码规则集合")
    @Excel(name = "条码规则集合名称", height = 20, width = 30)
    private String barcodeRuleName;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

}
