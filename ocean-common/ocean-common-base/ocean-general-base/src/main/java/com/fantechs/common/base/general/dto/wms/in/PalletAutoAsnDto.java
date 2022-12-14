package com.fantechs.common.base.general.dto.wms.in;

import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/5/20
 */
@Data
public class PalletAutoAsnDto extends WmsInAsnOrderDet implements Serializable {
    /**
     * 货主信息
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主信息")
    private Long materialOwnerId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 客户单号
     */
    @ApiModelProperty(name="customerOrderCode",value = "客户单号")
    private String customerOrderCode;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    private Date orderDate;

    /**
     * 预计到货(arrival of good)日期
     */
    @ApiModelProperty(name="planAgoDate",value = "预计到货(arrival of good)日期")
    private Date planAgoDate;


    /**
     * 发货人名称
     */
    @ApiModelProperty(name="shipperName",value = "发货人名称")
    private String shipperName;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系人电话")
    private String linkManPhone;

    /**
     * 传真号码
     */
    @ApiModelProperty(name="faxNumber",value = "传真号码")
    private String faxNumber;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="eMailAddress",value = "邮箱地址")
    private String eMailAddress;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="detailedAddress",value = "详细地址")
    private String detailedAddress;

    /**
     * 描述
     */
    @ApiModelProperty(name="descr",value = "描述")
    private String descr;

    /**
     * 堆垛ID
     */
    @ApiModelProperty(name = "stackingId",value = "堆垛ID")
    private Long stackingId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name = "workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 条码集合
     */
    @ApiModelProperty(name = "barCodeList",value = "条码集合")
    List<BarPODto> barCodeList;

    @ApiModelProperty(name = "customerName",value ="客户名称" )
    private String customerName;

    @ApiModelProperty(name = "salesManName",value = "业务员")
    private String salesManName;

    @ApiModelProperty(name = "salesOrderCode",value = "销售编码")
    private String salesOrderCode;

    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name = "logicId",value = "erp逻辑仓id")
    private Long logicId;

    @ApiModelProperty(name = "proLineId",value = "产线id")
    private Long proLineId;

    @ApiModelProperty(name = "workOrderCode",value = "离散任务号")
    private String workOrderCode;

    @ApiModelProperty(name = "samePackageCode" ,value = "PO")
    private String samePackageCode;

    @ApiModelProperty(name = "workOrderQty",value = "工单数量")
    private BigDecimal workOrderQty;
}
