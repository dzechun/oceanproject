package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseMaterialDto extends BaseMaterial implements Serializable {

    /**
     * 条码规则集合名称
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleSetName" ,value="条码规则集合名称")
    private String barcodeRuleSetName;

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

    /**
     * 电压
     */
    @Transient
    @ApiModelProperty(name="voltage",value = "电压")
    @Excel(name = "电压", height = 20, width = 30)
    private String voltage;

    /**
     *  客户型号编码
     */
    @Transient
    @ApiModelProperty(name="productModelCode",value = "客户型号编码")
    @Excel(name = "客户型号", height = 20, width = 30)
    private String productModelCode;

}
