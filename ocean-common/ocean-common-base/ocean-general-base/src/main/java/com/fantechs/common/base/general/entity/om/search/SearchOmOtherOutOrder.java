package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/23
 */
@Data
public class SearchOmOtherOutOrder extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "otherOutOrderCode",value = "订单号")
    private String otherOutOrderCode;

    @ApiModelProperty(name = "customerOrderCode",value = "客户单号")
    private String customerOrderCode;

    @ApiModelProperty(name = "orderStatus",value = "单据状态")
    private Byte orderStatus;

    /**
     * 订单日期开始时间
     */
    @ApiModelProperty(name="orderDateStartTime" ,value="开始时间(YYYY-MM-DD)")
    private String orderDateStartTime;

    /**
     * 订单日期结束时间
     */
    @ApiModelProperty(name="endTime" ,value="结束时间(YYYY-MM-DD)")
    private String orderDateEndTime;

    /**
     * 计划到达日期开始时间
     */
    @ApiModelProperty(name="planArriveDateStartTime" ,value="开始时间(YYYY-MM-DD)")
    private String planArriveDateStartTime;

    /**
     * 计划到达日期结束时间
     */
    @ApiModelProperty(name="planArriveDateEndTime" ,value="结束时间(YYYY-MM-DD)")
    private String planArriveDateEndTime;

    /**
     * 实际发运日期开始时间
     */
    @ApiModelProperty(name="actualDespatchDateStartTime" ,value="开始时间(YYYY-MM-DD)")
    private String actualDespatchDateStartTime;

    /**
     * 实际发运日期结束时间
     */
    @ApiModelProperty(name="actualDespatchDateEndTime" ,value="结束时间(YYYY-MM-DD)")
    private String actualDespatchDateEndTime;
}
