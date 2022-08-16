package com.fantechs.common.base.general.dto.basic;

import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSetDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseBarcodeRuleSetDetDto extends BaseBarcodeRuleSetDet implements Serializable {

    private static final long serialVersionUID = 6316384999118402692L;

    /**
     * 条码规则类别ID
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleCategoryId",value = "条码规则类别ID")
    private Long barcodeRuleCategoryId;

    /**
     * 条码规则类别名称
     */
    @Transient
    @ApiModelProperty(name = "barcodeRuleCategoryName",value = "条码规则类别名称")
    private String barcodeRuleCategoryName;

    /**
     * 条码规则
     */
    @Transient
    @ApiModelProperty(name="barcodeRule",value = "条码规则")
    private String barcodeRule;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
