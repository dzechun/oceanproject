package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEngPackingOrder extends BaseQuery implements Serializable {
    /**
     * 供应商编码
     */
    @ApiModelProperty(name = "supplierCode",value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    private String supplierName;


    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    private String despatchBatch;

    /**
     * 订单日期开始
     */
    @ApiModelProperty(name="orderTimeStart",value = "订单日期开始(yyyy-MM-dd HH:mm)")
    private String orderTimeStart;

    /**
     * 订单日期结束
     */
    @ApiModelProperty(name="orderTimeEnd",value = "订单日期结束(yyyy-MM-dd HH:mm)")
    private String orderTimeEnd;


    /**
     * 出厂时间开始
     */
    @ApiModelProperty(name="leaveFactoryTimeStart",value = "出厂时间开始(yyyy-MM-dd HH:mm)")
    private String leaveFactoryTimeStart;

    /**
     * 出厂时间结束
     */
    @ApiModelProperty(name="leaveFactoryTimeEnd",value = "出厂时间结束(yyyy-MM-dd HH:mm)")
    private String leaveFactoryTimeEnd;

    /**
     * 离港时间开始
     */
    @ApiModelProperty(name="leavePortTimeStart",value = "离港时间开始(yyyy-MM-dd HH:mm)")
    private String leavePortTimeStart;

    /**
     * 离港时间结束
     */
    @ApiModelProperty(name="leavePortTimeEnd",value = "离港时间结束(yyyy-MM-dd HH:mm)")
    private String leavePortTimeEnd;

    /**
     * 到港时间开始
     */
    @ApiModelProperty(name="arrivalPortTimeStart",value = "到港时间开始(yyyy-MM-dd HH:mm)")
    private String arrivalPortTimeStart;

    /**
     * 到港时间结束
     */
    @ApiModelProperty(name="arrivalPortTimeEnd",value = "到港时间结束(yyyy-MM-dd HH:mm)")
    private String arrivalPortTimeEnd;

    /**
     * 到场时间开始
     */
    @ApiModelProperty(name="arrivalTimeStart",value = "到场时间开始(yyyy-MM-dd HH:mm)")
    private String arrivalTimeStart;

    /**
     * 到场时间结束
     */
    @ApiModelProperty(name="arrivalTimeEnd",value = "到场时间结束(yyyy-MM-dd HH:mm)")
    private String arrivalTimeEnd;

    /**
     * 订单状态(1-未审核 2-审核中 3-已通过 4-未通过)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-未审核 2-审核中 3-已通过 4-未通过)")
    private Byte orderStatus;


    /**
     * 装箱单号
     */
    @ApiModelProperty(name="packingOrderCode",value = "装箱单号")
    private String packingOrderCode;

}
