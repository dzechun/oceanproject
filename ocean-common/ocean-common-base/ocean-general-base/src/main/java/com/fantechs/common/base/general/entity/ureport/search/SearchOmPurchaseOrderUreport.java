package com.fantechs.common.base.general.entity.ureport.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class SearchOmPurchaseOrderUreport extends BaseQuery implements Serializable {

    /**
     * 采购订单编码
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单编码")
    private String purchaseOrderCode;

    /**
     * 供应商编码
     */
    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 订单状态（1-开立 2-审核）
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态（1-开立 2-审核）")
    private Byte orderStatus;

    /**
     * 订单日期开始
     */
    @ApiModelProperty(name="orderDateStart",value = "订单日期开始")
    private Date orderDateStart;

    /**
     * 订单日期结束
     */
    @ApiModelProperty(name="orderDateEnd",value = "订单订单日期结束日期")
    private Date orderDateEnd;

    /**
     * 采购部门名称
     */
    @ApiModelProperty(name="deptName",value = "采购部门名称")
    private String deptName;

    /**
     * 制单人员名称
     */
    @ApiModelProperty(name="makeOrderUserName",value = "制单人员名称")
    private String makeOrderUserName;

    /**
     * 制单日期开始
     */
    @ApiModelProperty(name="makeOrderDateStart",value = "制单日期开始")
    private Date makeOrderDateStart;

    /**
     * 制单日期结束
     */
    @ApiModelProperty(name="makeOrderDateEnd",value = "制单日期结束")
    private Date makeOrderDateEnd;

    /**
     * 备注说明
     */
    @ApiModelProperty(name="remark",value = "备注说明")
    private String remark;

}
