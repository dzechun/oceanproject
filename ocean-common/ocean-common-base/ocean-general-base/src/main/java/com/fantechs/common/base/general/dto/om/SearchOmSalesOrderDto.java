package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchOmSalesOrderDto extends BaseQuery implements Serializable {
    /**
     * 销售订单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售订单号")
    private String salesOrderCode;
    /**
     * 客户订单号
     */
    @ApiModelProperty(value = "客户订单号", example = "客户订单号")
    private String customerOrderCode;
    /**
     * 合同号
     */
    @ApiModelProperty(value = "合同号", example = "合同号")
    private String contractCode;

    /**
     * 工单号
     */
    @ApiModelProperty(value = "合同号", example = "合同号")
    private Long workOrderId;

    @ApiModelProperty(name = "orderType",value = "订单类型")
    private String orderType;

    @ApiModelProperty(name = "salesUserName",value = "销售人员")
    private String salesUserName;
<<<<<<< .mine

    /**
     * 制单人员
     */
    @ApiModelProperty(name="createUserName",value = "制单人员")
    private String createUserName;

    /**
     * 审核人员
     */
    @ApiModelProperty(name="auditUserName",value = "审核人员")
    private String auditUserName;












=======

    /**
     * 制单人员
     */
    @ApiModelProperty(name="makeOrderUserName",value = "制单人员")
    private String makeOrderUserName;

    /**
     * 审核人员
     */
    @ApiModelProperty(name="auditUserName",value = "审核人员")
    private String auditUserName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 条码规则集合名称
     */
    @ApiModelProperty(name="barcodeRuleSetName", value = "条码规则集合名称")
    private String barcodeRuleSetName;
>>>>>>> .theirs
}
