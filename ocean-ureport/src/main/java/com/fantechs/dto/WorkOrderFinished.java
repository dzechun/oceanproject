package com.fantechs.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WorkOrderFinished implements Serializable {

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="1")
    private String workOrderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractNo",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="2")
    private String contractNo;

    /**
     * 订单号
     */
    @ApiModelProperty(name="orderCode" ,value="订单号")
    @Excel(name = "订单号", height = 20, width = 30,orderNum="3")
    private String orderCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="supplierName" ,value="客户名称")
    @Excel(name = "客户名称", height = 20, width = 30,orderNum="4")
    private String supplierName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="5")
    private String materialCode;

    /**
     * 物料型号
     */
    @ApiModelProperty(name="productModelName" ,value="物料型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="6")
    private String productModelName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30,orderNum="7")
    private String materialDesc;

    /**
     * 线别代码
     */
    @ApiModelProperty(name="proCode" ,value="线别代码")
    @Excel(name = "产线", height = 20, width = 30,orderNum="8")
    private String proCode;

    /**
     * 工单状态(0、待生产 1、待首检 2、生产中 3、暂停生产 4、生产完成 5、工单挂起)
     */
    @ApiModelProperty(name="workOrderStatus" ,value="工单状态(0、待生产 1、待首检 2、生产中 3、暂停生产 4、生产完成 5、工单挂起)")
    @Excel(name = "工单状态", height = 20, width = 30 ,orderNum="9",replace = {"待生产_0", "待首检_2", "生产中_2","暂停生产_3","生产完成_4","工单挂起_5"})
    private Integer workOrderStatus;

    /**
     * 订单箱数
     */
    @ApiModelProperty(name="orderBoxQuantity" ,value="订单箱数")
    @Excel(name = "订单箱数", height = 20, width = 30,orderNum="10")
    private BigDecimal orderBoxQuantity;

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQuantity" ,value="订单数量")
    @Excel(name = "订单总数", height = 20, width = 30,orderNum="11")
    private BigDecimal orderQuantity;

    /**
     * 排产箱数
     */
    @ApiModelProperty(name="schBoxQuantity" ,value="排产箱数")
    @Excel(name = "排产箱数", height = 20, width = 30,orderNum="12")
    private BigDecimal schBoxQuantity;

    /**
     * 排产总数
     */
    @ApiModelProperty(name="schTotal" ,value = "排产总数")
    @Excel(name = "排产总数", height = 20, width = 30,orderNum="13")
    private BigDecimal schTotal;

    /**
     * 生产箱数
     */
    @ApiModelProperty(name="proBoxQuantity" ,value="生产箱数")
    @Excel(name = "生产箱数", height = 20, width = 30,orderNum="14")
    private BigDecimal proBoxQuantity;

    /**
     * 生产总数
     */
    @ApiModelProperty(name="proBoxQuantity" ,value="生产总数")
    @Excel(name = "生产总数", height = 20, width = 30,orderNum="15")
    private BigDecimal proQuantity;

    /**
     * 出货剩余箱数
     */
    @ApiModelProperty(name="outSurplusBoxQuantity" ,value="出货剩余箱数")
    @Excel(name = "出货剩余箱数", height = 20, width = 30,orderNum="16")
    private BigDecimal outSurplusBoxQuantity;

    /**
     * 出货剩余总数
     */
    @ApiModelProperty(name="outSurplusQuantity" ,value="出货剩余总数")
    @Excel(name = "出货剩余总数", height = 20, width = 30,orderNum="17")
    private BigDecimal outSurplusQuantity;

    /**
     * 操作人
     */
    @ApiModelProperty(name = "userName",value = "操作人")
    @Excel(name = "操作人", height = 20, width = 30,orderNum="18")
    private String userName;

    /**
     * 操作时间
     */
    @ApiModelProperty(name="createTime" ,value="操作时间")
    @Excel(name = "操作时间", height = 20, width = 30,orderNum="19")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
