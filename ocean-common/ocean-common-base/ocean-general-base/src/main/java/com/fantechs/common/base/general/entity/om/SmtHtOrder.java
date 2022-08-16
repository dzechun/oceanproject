package com.fantechs.common.base.general.entity.om;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;

@Table(name = "base_ht_order")
@Data
public class SmtHtOrder implements Serializable {
    /**
    * 历史订单ID
    */
    @ApiModelProperty(value = "历史订单ID",example = "历史订单ID")
    @Column(name = "ht_order_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    @Excel(name = "历史订单ID")
    private Long htOrderId;

    /**
    * 订单ID
    */
    @ApiModelProperty(value = "订单ID",example = "订单ID")
    @Column(name = "order_id")
    @Excel(name = "订单ID")
    private Long orderId;

    /**
    * 订单号
    */
    @ApiModelProperty(value = "订单号",example = "订单号")
    @Column(name = "order_code")
    @Excel(name = "订单号")
    private String orderCode;

    /**
    * 合同号
    */
    @ApiModelProperty(value = "合同号",example = "合同号")
    @Column(name = "contract_code")
    @Excel(name = "合同号")
    private String contractCode;

    /**
    * 客户ID
    */
    @ApiModelProperty(value = "客户ID",example = "客户ID")
    @Column(name = "customer_id")
    @Excel(name = "客户ID")
    private Long supplierId;

    /**
    * 产品料号ID
    */
    @ApiModelProperty(value = "产品料号ID",example = "产品料号ID")
    @Column(name = "material_id")
    @Excel(name = "产品料号ID")
    private Long materialId;

    /**
    * 订单数量
    */
    @ApiModelProperty(value = "订单数量",example = "订单数量")
    @Column(name = "order_quantity")
    @Excel(name = "订单数量")
    private Integer orderQuantity;

    /**
    * 已交付数量
    */
    @ApiModelProperty(value = "已交付数量",example = "已交付数量")
    @Column(name = "delivered_quantity")
    @Excel(name = "已交付数量")
    private Integer deliveredQuantity;

    /**
    * 喷绘信息
    */
    @ApiModelProperty(value = "喷绘信息",example = "喷绘信息")
    @Column(name = "inkjet_paint")
    @Excel(name = "喷绘信息")
    private String inkjetPaint;

    /**
    * 镭雕信息
    */
    @ApiModelProperty(value = "镭雕信息",example = "镭雕信息")
    @Column(name = "radium_carving")
    @Excel(name = "镭雕信息")
    private String radiumCarving;

    /**
    * 排产交期
    */
    @ApiModelProperty(value = "排产交期",example = "排产交期")
    @Column(name = "schedule_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "排产交期")
    private java.util.Date scheduleTime;

    /**
    * 交货日期
    */
    @ApiModelProperty(value = "交货日期",example = "交货日期")
    @Column(name = "finished_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交货日期")
    private java.util.Date finishedTime;

    /**
    * 状态（0、创建 1、下载完成 2、已完工 3、作废）
    */
    @ApiModelProperty(value = "状态（0、创建 1、下载完成 2、已完工 3、作废）",example = "状态（0、创建 1、下载完成 2、已完工 3、作废）")
    @Excel(name = "状态（0、创建 1、下载完成 2、已完工 3、作废）")
    private Byte status;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",example = "备注")
    @Excel(name = "备注")
    private String remark;

    /**
    * 组织id
    */
    @ApiModelProperty(value = "组织id",example = "组织id")
    @Column(name = "org_id")
    @Excel(name = "组织id")
    private Long organizationId;

    /**
    * 交货日期
    */
    @ApiModelProperty(value = "交货日期",example = "交货日期")
    @Column(name = "delivery_date")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交货日期")
    private java.util.Date deliveryDate;

    /**
    * 业务员名称
    */
    @ApiModelProperty(value = "业务员名称",example = "业务员名称")
    @Column(name = "sales_man_name")
    @Excel(name = "业务员名称")
    private String salesManName;

    /**
    * 照片链接
    */
    @ApiModelProperty(value = "照片链接",example = "照片链接")
    @Column(name = "photo_url")
    @Excel(name = "照片链接")
    private String photoUrl;

    /**
    * 创建人ID
    */
    @ApiModelProperty(value = "创建人ID",example = "创建人ID")
    @Column(name = "create_user_id")
    @Excel(name = "创建人ID")
    private Long createUserId;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",example = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    private java.util.Date createTime;

    /**
    * 修改人ID
    */
    @ApiModelProperty(value = "修改人ID",example = "修改人ID")
    @Column(name = "modified_user_id")
    @Excel(name = "修改人ID")
    private Long modifiedUserId;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间",example = "修改时间")
    @Column(name = "modified_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间")
    private java.util.Date modifiedTime;

    /**
    * 逻辑删除（0、删除 1、正常）
    */
    @ApiModelProperty(value = "逻辑删除（0、删除 1、正常）",example = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    @Excel(name = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
    * 扩展字段1
    */
    @ApiModelProperty(value = "扩展字段1",example = "扩展字段1")
    @Excel(name = "扩展字段1")
    private String option1;

    /**
    * 扩展字段2
    */
    @ApiModelProperty(value = "扩展字段2",example = "扩展字段2")
    @Excel(name = "扩展字段2")
    private String option2;

    /**
    * 扩展字段3
    */
    @ApiModelProperty(value = "扩展字段3",example = "扩展字段3")
    @Excel(name = "扩展字段3")
    private String option3;

    /**
     * 客货号
     */
    @Column(name = "freight_num")
    @ApiModelProperty(name="freightNum" ,value="客货号")
    private String freightNum;

}