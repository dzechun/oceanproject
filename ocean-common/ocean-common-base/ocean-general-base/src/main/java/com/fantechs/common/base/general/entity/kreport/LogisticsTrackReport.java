package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class LogisticsTrackReport extends ValidGroup implements Serializable {

    @Id
    private Long id;

    /**
     * 公司名称
     */
    @ApiModelProperty(name="companyName",value = "公司名称")
    private String companyName;

    /**
     * 公司编码
     */
    @ApiModelProperty(name="companyCode",value = "公司编码")
    private String companyCode;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 单据类型
     */
    @ApiModelProperty(name="billType",value = "单据类型")
    private String billType;

    /**
     * WMS单据号
     */
    @ApiModelProperty(name="billCode",value = "WMS单据号")
    private String billCode;

    /**
     * 收发类型
     */
    @ApiModelProperty(name="type",value = "收发类型")
    private String type;

    /**
     * 单据来源
     */
    @ApiModelProperty(name="billSource",value = "单据来源")
    private String billSource;

    /**
     * 来源单据
     */
    @ApiModelProperty(name="relatedBill",value = "来源单据")
    private String relatedBill;

    /**
     * 订单生成时间
     */
    @ApiModelProperty(name="orderDate",value = "订单生成时间")
    private String orderDate;

    /**
     * 订单完成时间
     */
    @ApiModelProperty(name="orderCompletionDate",value = "订单完成时间")
    private String orderCompletionDate;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchNumber",value = "批次号")
    private String batchNumber;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    private String planQty;

    /**
     * 实际数量
     */
    @ApiModelProperty(name="realityQty",value = "实际数量")
    private String realityQty;

    /**
     * 承运商编码
     */
    @ApiModelProperty(name="carrierCode",value = "承运商编码")
    private String carrierCode;

    /**
     * 承运商名称
     */
    @ApiModelProperty(name="carrierName",value = "承运商名称")
    private String carrierName;

    /**
     * 物流单号
     */
    @ApiModelProperty(name="logisticsCode",value = "物流单号")
    private String logisticsCode;

    /**
     * 物流件数
     */
    @ApiModelProperty(name="logisticsNumber",value = "物流件数")
    private String logisticsNumber;

    /**
     * 订单备注
     */
    @ApiModelProperty(name="orderRemark",value = "订单备注")
    private String orderRemark;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="customerName",value = "客户名称")
    private String customerName;

    /**
     * 收货人
     */
    @ApiModelProperty(name="consignee",value = "收货人")
    private String consignee;

    /**
     * 收货人电话
     */
    @ApiModelProperty(name="contactTelephone",value = "收货人电话")
    private String contactTelephone;

    /**
     * 收货地址
     */
    @ApiModelProperty(name="contactAddress",value = "收货地址")
    private String contactAddress;

    /**
     * 揽件人
     */
    @ApiModelProperty(name="pullPerson",value = "揽件人")
    private String pullPerson;

    /**
     * 揽件人电话
     */
    @ApiModelProperty(name="pullPersonPhone",value = "揽件人电话")
    private String pullPersonPhone;

    /**
     * 收件人
     */
    @ApiModelProperty(name="recipients",value = "收件人")
    private String recipients;

    /**
     * 收件人电话
     */
    @ApiModelProperty(name="recipientsPhone",value = "收件人电话")
    private String recipientsPhone;

    /**
     * 揽件时间
     */
    @ApiModelProperty(name="pullDate",value = "揽件时间")
    private String pullDate;

    /**
     * 收件时间
     */
    @ApiModelProperty(name="recipientsDate",value = "收件时间")
    private String recipientsDate;

    /**
     * 物流轨迹详情
     */
    @ApiModelProperty(name="routeTrace",value = "物流轨迹详情")
    private String routeTrace;

    /**
     * 轨迹详情来源
     */
    @ApiModelProperty(name="routeTraceSource",value = "轨迹详情来源")
    private String routeTraceSource;

}
