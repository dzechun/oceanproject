package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class OmHtSalesOrderDto extends OmHtSalesOrder implements Serializable {
    /**
     * 销售部门
     */
    @Transient
    @ApiModelProperty(name = "salesDept", value = "销售部门")
    @Excel(name = "销售部门", height = 20, width = 30, orderNum = "18")
    private String salesDept;

    /**
     * 销售人员
     */
    @Transient
    @ApiModelProperty(name = "salesUserName", value = "销售人员")
    @Excel(name = "销售人员", height = 20, width = 30, orderNum = "18")
    private String salesUserName;

    /**
     * 制单人员
     */
    @Transient
    @ApiModelProperty(name = "makeOrderUserName", value = "制单人员")
    @Excel(name = "制单人员", height = 20, width = 30, orderNum = "18")
    private String makeOrderUserName;

    /**
     * 审核人员
     */
    @Transient
    @ApiModelProperty(name = "auditUserName", value = "审核人员")
    @Excel(name = "审核人员", height = 20, width = 30, orderNum = "18")
    private String auditUserName;

    @Transient
    @ApiModelProperty(name = "supplierName", value = "客户名称")
//    @Excel(name = "客户名称", height = 20, width = 30)
    private String supplierName;

    @Transient
    @ApiModelProperty(name = "barcodeRuleSetName", value = "条码规则集合名称")
//    @Excel(name = "条码规则集合名称", height = 20, width = 30)
    private String barcodeRuleSetName;
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
//    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
//    @Excel(name = "创建账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
//    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;
}
