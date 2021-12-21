package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmHtPurchaseOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OmHtPurchaseOrderDto extends OmHtPurchaseOrder implements Serializable {

    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(name="eMail",value = "供应商邮箱")
    private String eMail;

    @ApiModelProperty(name="supplierName",value = "供应商手机号")
    private String mobilePhone;

    @ApiModelProperty(name="telephone",value = "供应商联系电话")
    private String telephone;

    @ApiModelProperty(name="completeDetail",value = "供应商联系电话")
    private String completeDetail;

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

    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

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

    List<OmPurchaseOrderDetDto> omPurchaseOrderDetDtos;

}
