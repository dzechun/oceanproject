package com.fantechs.common.base.general.dto.basic;

import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseBarcodeRuleSpecDto extends BaseBarcodeRuleSpec implements Serializable {

    private static final long serialVersionUID = -2391468733686447507L;

    /**
     * 条码规则
     */
    @Transient
    @ApiModelProperty(name="barcodeRule",value = "条码规则")
    private String barcodeRule;

    /**
     * 属性类别
     */
    @Transient
    @ApiModelProperty(name="specName",value = "属性类别")
    private String specName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
