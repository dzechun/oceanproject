package com.fantechs.common.base.general.entity.om.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
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
     * 要求到达日期开始时间
     */
    @ApiModelProperty(name="reqArriveDateT" ,value="要求到达日期开始时间(YYYY-MM-DD)")
    private String reqArriveDateT;

    /**
     * 要求到达日期结束时间
     */
    @ApiModelProperty(name="reqArriveDateF" ,value="要求到达日期结束时间(YYYY-MM-DD)")
    private String reqArriveDateF;

    /**
     * 计划到达日期开始时间
     */
    @ApiModelProperty(name="planArriveDateT" ,value="计划到达日期开始时间(YYYY-MM-DD)")
    private String planArriveDateT;

    /**
     * 计划到达日期结束时间
     */
    @ApiModelProperty(name="planArriveDateF" ,value="计划到达日期结束时间(YYYY-MM-DD)")
    private String planArriveDateF;

    /**
     * 实际发运日期开始时间
     */
    @ApiModelProperty(name="actualDespatchDateT" ,value="实际发运日期开始时间(YYYY-MM-DD)")
    private String actualDespatchDateT;

    /**
     * 实际发运日期结束时间
     */
    @ApiModelProperty(name="actualDespatchDateF" ,value="实际发运日期结束时间(YYYY-MM-DD)")
    private String actualDespatchDateF;


    /**
     * 客户
     */
    @ApiModelProperty(name = "supplierName",value = "客户")
    private String supplierName;

    /**
     * 收货人
     */
    @ApiModelProperty(name = "consigneeName",value = "收货人")
    private String consigneeName;

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
     * 传真
     */
    @ApiModelProperty(name="faxNumber",value = "传真")
    private String faxNumber;

    /**
     * 邮件地址
     */
    @ApiModelProperty(name="eMailAddress",value = "邮件地址")
    private String eMailAddress;

    /**
     * 地址
     */
    @ApiModelProperty(name="address",value = "地址")
    private String address;


    /**
     * 货主
     */
    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;
}
