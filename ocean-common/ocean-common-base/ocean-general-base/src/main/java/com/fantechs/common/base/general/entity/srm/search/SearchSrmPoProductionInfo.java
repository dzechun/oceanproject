package com.fantechs.common.base.general.entity.srm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SearchSrmPoProductionInfo extends BaseQuery implements Serializable {

    /**
     * 采购订单ID
     */
    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    private Long purchaseOrderId;

    /**
     * 采购订单号
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    private String purchaseOrderCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 产品版本
     */
    @ApiModelProperty(name="materialVersion",value = "产品版本")
    private String materialVersion;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="materialDesc",value = "产品描述")
    private String materialDesc;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    private String materialName;

    /**
     * 工单数量
     */
    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    private BigDecimal workOrderQty;

    /**
     * 完工数量
     */
    @ApiModelProperty(name="outputQty",value = "完工数量")
    private BigDecimal outputQty;

    /**
     * 工单状态(1-待生产、2-生产中、3-完工)
     */
    @ApiModelProperty(name="workOrderStatus",value = "工单状态(1-待生产、2-生产中、3-完工)")
    private Byte workOrderStatus;

    /**
     * 实际开始时间-开始
     */
    @ApiModelProperty(name="actualStartTimeStart",value = "实际开始时间-开始")
    private String actualStartTimeStart;

    /**
     * 实际开始时间-结束
     */
    @ApiModelProperty(name="actualStartTimeEnd",value = "实际开始时间-结束")
    private String actualStartTimeEnd;

    /**
     * 实际结束时间-开始
     */
    @ApiModelProperty(name="actualEndTimeStart",value = "实际结束时间-开始")
    private String actualEndTimeStart;

    /**
     * 实际结束时间-结束
     */
    @ApiModelProperty(name="actualEndTimeEnd",value = "实际结束时间-结束")
    private String actualEndTimeEnd;

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

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
