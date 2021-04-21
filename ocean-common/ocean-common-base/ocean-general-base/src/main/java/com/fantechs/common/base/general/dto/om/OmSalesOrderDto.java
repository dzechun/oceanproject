package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class OmSalesOrderDto {
    /**
     * 订单状态
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态")
    @Excel(name = "订单状态", height = 20, width = 30,orderNum="")
    private Byte orderStatus;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Excel(name = "客户ID", height = 20, width = 30,orderNum="")
    private Long supplierId;

    /**
     * 客户订单号
     */
    @ApiModelProperty(name="customerOrderCode",value = "客户订单号")
    @Excel(name = "客户订单号", height = 20, width = 30,orderNum="")
    private String customerOrderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="")
    private String contractCode;

    /**
     * 来源单据号
     */
    @ApiModelProperty(name="sourceOrderId",value = "来源单据号")
    @Excel(name = "来源单据号", height = 20, width = 30,orderNum="")
    private Long sourceOrderId;

    /**
     * 来源单据行
     */

    /**
     * 交货方式
     */
    @ApiModelProperty(name="deliveryType",value = "交货方式")
    @Excel(name = "交货方式", height = 20, width = 30,orderNum="")
    private String deliveryType;

    /**
     * 销售部门
     */
    @ApiModelProperty(name="salesDept",value = "销售部门")
    @Excel(name = "销售部门", height = 20, width = 30,orderNum="")
    private String salesDept;

    /**
     * 销售人员
     */
    @ApiModelProperty(name="salesUserName",value = "销售人员")
    @Excel(name = "销售人员", height = 20, width = 30,orderNum="")
    private String salesUserName;

    /**
     * 数据来源
     */
    @ApiModelProperty(name="dataSource",value = "数据来源")
    @Excel(name = "数据来源", height = 20, width = 30,orderNum="")
    private String dataSource;

    /**
     * 订单类型
     */
    @ApiModelProperty(name="orderType",value = "订单类型")
    @Excel(name = "订单类型", height = 20, width = 30,orderNum="")
    private Byte orderType;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="")
    private Date orderDate;

    /**
     * 制单人员
     */
    @ApiModelProperty(name="makeOrderUserName",value = "制单人员")
    @Excel(name = "制单人员", height = 20, width = 30,orderNum="")
    private String makeOrderUserName;

    /**
     * 制单日期
     */
    @ApiModelProperty(name="makeOrderDate",value = "制单日期")
    @Excel(name = "制单日期", height = 20, width = 30,orderNum="")
    private Date makeOrderDate;

    /**
     * 审核人员
     */
    @ApiModelProperty(name="auditUserName",value = "审核人员")
    @Excel(name = "审核人员", height = 20, width = 30,orderNum="")
    private String auditUserName;

    /**
     * 审核日期
     */
    @ApiModelProperty(name="auditDate",value = "审核日期")
    @Excel(name = "审核日期", height = 20, width = 30,orderNum="")
    private Date auditDate;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId",value = "条码规则集合ID")
    @Excel(name = "条码规则集合ID", height = 20, width = 30,orderNum="")
    private Long barcodeRuleSetId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    private String remark;
}
