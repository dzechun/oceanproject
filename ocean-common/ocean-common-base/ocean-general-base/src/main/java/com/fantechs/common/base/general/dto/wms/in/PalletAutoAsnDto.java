package com.fantechs.common.base.general.dto.wms.in;

import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/5/20
 */
@Data
public class PalletAutoAsnDto extends SrmInAsnOrderDet implements Serializable {
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
     * 车间管理模块栈板表ID
     */
    @ApiModelProperty(name = "productPalletId",value = "车间管理模块栈板表ID")
    private Long productPalletId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name = "workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 条码集合
     */
    @ApiModelProperty(name = "barCodeList",value = "条码集合")
    List<String> barCodeList;
}
