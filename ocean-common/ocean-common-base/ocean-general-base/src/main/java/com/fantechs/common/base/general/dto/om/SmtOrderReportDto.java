package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2021/3/25
 */
@Data
public class SmtOrderReportDto implements Serializable {

    /**
     * 订单号
     */
    @ApiModelProperty(name="orderCode" ,value="订单号")
    @Excel(name = "订单号", height = 20, width = 30,orderNum="1")
    private String orderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode" ,value="合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="2")
    private String contractCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="supplierName" ,value="客户名称")
    @Excel(name = "客户名称", height = 20, width = 30,orderNum="3")
    private String supplierName;

    /**
     * 业务员名称
     */
    @ApiModelProperty(name="salesManName" ,value="业务员名称")
    @Excel(name = "业务员名称", height = 20, width = 30,orderNum="4")
    private String salesManName;

    /**
     * 合同交期
     */
    @ApiModelProperty(name="contractDate" ,value="合同交期")
    @Excel(name = "交货日期", height = 20, width = 30,orderNum="5")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractDate;

    /**
     * 排产日期
     */
    @ApiModelProperty(name="schedule_date" ,value="排产日期")
    @Excel(name = "排产日期", height = 20, width = 30,orderNum="6")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    /**
     * 排产交期
     */
    @ApiModelProperty(name="deliveryDate" ,value="排产交期")
    @Excel(name = "排产交期", height = 20, width = 30,orderNum="7")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="8")
    private String materialCode;


    /**
     * 物料型号
     */
    @Transient
    @ApiModelProperty(name="productModelName" ,value="物料型号")
    @Excel(name = "物料型号", height = 20, width = 30,orderNum="9")
    private String productModelName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30,orderNum="10")
    private String materialDesc;

    /**
     * 订单箱数
     */
    @Transient
    @ApiModelProperty(name="workOrderBox" ,value="订单箱数")
    @Excel(name = "订单箱数", height = 20, width = 30,orderNum="11")
    private BigDecimal workOrderBox;

    /**
     * 订单把数
     */
    @Transient
    @ApiModelProperty(name="work_order_quantity" ,value="订单把数")
    @Excel(name = "订单把数", height = 20, width = 30,orderNum="12")
    private BigDecimal workOrderQuantity;

    /**
     * 订单状态
     */
    @Transient
    @ApiModelProperty(name="work_order_status" ,value="订单状态")
    @Excel(name = "订单状态", height = 20, width = 30,orderNum="13")
    private String workOrderStatus;


    /**
     * 出货箱数
     */
    @Transient
    @ApiModelProperty(name="outBox" ,value="出货箱数")
    @Excel(name = "出货箱数", height = 20, width = 30,orderNum="14")
    private BigDecimal outBox;

    /**
     * 出货把数
     */
    @Transient
    @ApiModelProperty(name="output_quantity" ,value="出货把数")
    @Excel(name = "出货把数", height = 20, width = 30,orderNum="15")
    private BigDecimal outputQuantity;


    /**
     * 剩余箱数
     */
    @Transient
    @ApiModelProperty(name="surplusBox" ,value="剩余箱数")
    @Excel(name = "剩余箱数", height = 20, width = 30,orderNum="16")
    private BigDecimal surplusBox;

    /**
     * 剩余把数
     */
    @Transient
    @ApiModelProperty(name="surplusQty" ,value="剩余把数")
    @Excel(name = "剩余把数", height = 20, width = 30,orderNum="17")
    private BigDecimal surplusQty;
}
