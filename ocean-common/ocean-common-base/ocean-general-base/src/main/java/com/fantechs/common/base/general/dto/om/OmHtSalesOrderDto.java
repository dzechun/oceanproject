package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class OmHtSalesOrderDto extends OmHtSalesOrder implements Serializable {
    @ApiModelProperty(name = "supplierName", value = "客户名称")
//    @Excel(name = "客户名称", height = 20, width = 30)
    private String supplierName;

    @ApiModelProperty(name = "barcodeRuleSetName", value = "条码规则集合名称")
//    @Excel(name = "条码规则集合名称", height = 20, width = 30)
    private String barcodeRuleSetName;
    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
//    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;
    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
//    @Excel(name = "创建账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName",value = "组织名称")
//    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;
}
