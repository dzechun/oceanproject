package com.fantechs.common.base.dto.apply;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.apply.SmtBarcodeRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SmtBarcodeRuleDto extends SmtBarcodeRule implements Serializable {

    private static final long serialVersionUID = -762872718021433489L;

    /**
     * 条码规则类别名称
     */
    @Transient
    @ApiModelProperty(name = "barcodeRuleCategoryName",value = "条码规则类别名称")
    @Excel(name = "条码规则类别", height = 20, width = 30,orderNum="4")
    private String barcodeRuleCategoryName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;
}
