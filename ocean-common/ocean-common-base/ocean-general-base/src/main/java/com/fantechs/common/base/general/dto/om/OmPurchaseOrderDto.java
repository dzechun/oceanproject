package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OmPurchaseOrderDto extends OmPurchaseOrder implements Serializable {

    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="2")
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
    @Excel(name = "采购部门名称", height = 20, width = 30,orderNum="4")
    private String deptName;

    @ApiModelProperty(name="purchaseUserName",value = "采购人员名称")
    @Excel(name = "采购人员名称", height = 20, width = 30,orderNum="3")
    private String purchaseUserName;

    @ApiModelProperty(name="makeOrderUserName",value = "制单人员名称")
    private String makeOrderUserName;

    @ApiModelProperty(name="auditUserName",value = "审核人员名称")
    private String auditUserName;

    @ApiModelProperty(name="currencyName",value = "货币名称")
    private String currencyName;

    @ApiModelProperty(name="modifiedUserName",value = "修改人员名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="5")
    private String warehouseName;

    /**
     * 创建名称
     */
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="8")
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
