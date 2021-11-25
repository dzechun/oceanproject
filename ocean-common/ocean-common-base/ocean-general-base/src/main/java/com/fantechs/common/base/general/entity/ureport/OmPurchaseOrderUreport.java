package com.fantechs.common.base.general.entity.ureport;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmPurchaseOrderUreport implements Serializable {

    /**
     * 采购订单ID
     */
    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    @Id
    private Long purchaseOrderId;

    /**
     * 采购订单编码
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单编码")
    @Excel(name = "采购订单编码", height = 20, width = 30)
    private String purchaseOrderCode;

    /**
     * 供应商编码
     */
    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    @Excel(name = "供应商编码", height = 20, width = 30)
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30)
    private String supplierName;

    /**
     * 订单状态（1-开立 2-审核）
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态（1-开立 2-审核）")
    @Excel(name = "订单状态", height = 20, width = 30,replace = {"开立_1","审核_2"})
    private Byte orderStatus;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;

    /**
     * 采购部门名称
     */
    @ApiModelProperty(name="deptName",value = "采购部门名称")
    @Excel(name = "采购部门", height = 20, width = 30)
    private String deptName;

    /**
     * 制单人员名称
     */
    @ApiModelProperty(name="makeOrderUserName",value = "制单人员名称")
    @Excel(name = "制单人员", height = 20, width = 30)
    private String makeOrderUserName;

    /**
     * 制单日期
     */
    @ApiModelProperty(name="makeOrderDate",value = "制单日期")
    @Excel(name = "制单日期", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date makeOrderDate;

    /**
     * 备注说明
     */
    @ApiModelProperty(name="remark",value = "备注说明")
    @Excel(name = "备注说明", height = 20, width = 30)
    private String remark;

}
