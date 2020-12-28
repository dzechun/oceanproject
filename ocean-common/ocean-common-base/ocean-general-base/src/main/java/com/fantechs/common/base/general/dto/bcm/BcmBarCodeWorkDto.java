package com.fantechs.common.base.general.dto.bcm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2020/12/22
 */
@Data
public class BcmBarCodeWorkDto extends BcmBarCode implements Serializable {

    /**
     * 工单id
     */
    @Transient
    @ApiModelProperty(name="workOrderId" ,value="工单号")
    @Excel(name = "工单id", height = 20, width = 30,orderNum="1")
    private Long workOrderId;
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="1")
    private String workOrderCode;
    /**
     * 工单类别
     */
    @Transient
    @ApiModelProperty(name="workOrderType" ,value="工单号")
    @Excel(name = "工单类别", height = 20, width = 30,orderNum="1")
    private String workOrderType;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="零件料号")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30,orderNum="3")
    private String version;

    /**
     * 工单数量
     */
    @Column(name = "work_order_quantity")
    @ApiModelProperty(name="workOrderQuantity" ,value="工单数量")
    @NotNull(message = "工单数量不能为空")
    @Excel(name = "工单数量", height = 20, width = 30,orderNum="5")
    private Integer workOrderQuantity;

    /**
     * 投产数量
     */
    @Column(name = "production_quantity")
    @ApiModelProperty(name="productionQuantity" ,value="投产数量")
    @Excel(name = "投产数量", height = 20, width = 30,orderNum="6")
    private Integer productionQuantity;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="10")
    private String routeName;

    /**
     * 投入工序
     */
    @Transient
    @ApiModelProperty(name="putIntoProcessName" ,value="投入工序")
    private String putIntoProcessName;

    /**
     * 产出工序
     */
    @Transient
    @ApiModelProperty(name="productionProcessName" ,value="产出工序")
    private String productionProcessName;

    /**
     * 计划结束时间
     */
    @Column(name = "planned_end_time")
    @ApiModelProperty(name="plannedEndTime" ,value="计划结束时间")
    @Excel(name = "计划结束时间", height = 20, width = 30,orderNum="14",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plannedEndTime;

    /**
     * 客户名称
     */
    @Transient
    @ApiModelProperty(name="customerName" ,value="客户名称")
    @Excel(name = "客户名称", height = 20, width = 30,orderNum="2")
    private String customerName;

    /**
     * 订单号
     */
    @Column(name = "order_code")
    @NotBlank(message = "订单号不能为空")
    @ApiModelProperty(name="orderCode" ,value="订单号")
    @Excel(name = "订单号", height = 20, width = 30,orderNum="1")
    private String orderCode;

    /**
     * 转移批量
     */
    @Transient
    @ApiModelProperty(name="transferQuantity" ,value="转移批量")
    private Integer transferQuantity;

    /**
     * 条码规则编码
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleCode" ,value="条码规则编码")
    private String barcodeRuleCode;

    /**
     * 条码/流转卡 解析码
     */
    @Transient
    @ApiModelProperty(name = "barcode",value = "条码/流转卡 解析码")
    private String barcode;
}
