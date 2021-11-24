package com.fantechs.common.base.general.entity.srm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
public class SearchSrmPoExpedite extends BaseQuery implements Serializable {

    /**
     * 采购订单号
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    private String purchaseOrderCode;

    /**
     * 单据状态
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态")
    private Byte orderStatus;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 订单日期开始
     */
    @ApiModelProperty(name="orderDateStart",value = "订单日期开始")
    private String orderDateStart;

    /**
     * 订单日期结束
     */
    @ApiModelProperty(name="orderDateEnd",value = "订单日期结束")
    private String orderDateEnd;

    /**
     * 采购部门
     */
    @ApiModelProperty(name="purchaseDeptName",value = "采购部门")
    private String purchaseDeptName;

    /**
     * 制单人
     */
    @ApiModelProperty(name="makeOrderUserName",value = "制单人")
    private String makeOrderUserName;

    /**
     * 制单日期开始
     */
    @ApiModelProperty(name="makeOrderDateStart",value = "制单日期开始")
    private String makeOrderDateStart;

    /**
     * 制单日期结束
     */
    @ApiModelProperty(name="makeOrderDateEnd",value = "制单日期结束")
    private String makeOrderDateEnd;


    /**
     * 备注说明
     */
    @ApiModelProperty(name="orderRemark",value = "备注说明")
    private String orderRemark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间开始
     */
    @ApiModelProperty(name="modifiedTimeStart",value = "修改时间开始")
    private String modifiedTimeStart;

    /**
     * 修改时间结束
     */
    @ApiModelProperty(name="modifiedTimeEnd",value = "修改时间结束")
    private String modifiedTimeEnd;

    private static final long serialVersionUID = 1L;
}
