package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OmPurchaseOrderDto extends OmPurchaseOrder implements Serializable {

    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(name="deptCode",value = "采购部门编码")
    private String deptCode;

    @ApiModelProperty(name="deptName",value = "采购部门名称")
    private String deptName;

    @ApiModelProperty(name="purchaseUserName",value = "采购人员名称")
    private String purchaseUserName;

    @ApiModelProperty(name="makeOrderUserName",value = "制单人员名称")
    private String makeOrderUserName;

    @ApiModelProperty(name="auditUserName",value = "审核人员名称")
    private String auditUserName;

    @ApiModelProperty(name="currencyName",value = "货币名称")
    private String currencyName;

    @ApiModelProperty(name="modifiedUserName",value = "修改人员名称")
    private String modifiedUserName;

    /**
     * 创建名称
     */
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    private String createUserName;

    /**
     * 组织
     */
    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;

    /**
     * 总金额
     */
    @ApiModelProperty(name="TotalPrice",value = "总金额")
    private BigDecimal totalPrice;
}
