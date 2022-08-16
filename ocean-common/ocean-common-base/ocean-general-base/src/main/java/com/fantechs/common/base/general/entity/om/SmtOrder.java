package com.fantechs.common.base.general.entity.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_order")
@Data
public class  SmtOrder extends ValidGroup implements Serializable {
    /**
     * 订单ID
     */
    @Id
    @Column(name = "order_id")
    @NotNull(groups = update.class,message = "订单id不能为空")
    @ApiModelProperty(name="orderId" ,value="订单ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long orderId;

    /**
     * 订单号
     */
    @Column(name = "order_code")
    @ApiModelProperty(name="orderCode" ,value="订单号")
    @Excel(name = "订单号", height = 20, width = 30,orderNum="1")
    private String orderCode;

    /**
     * 合同号
     */
    @Column(name = "contract_code")
    @ApiModelProperty(name="contractCode" ,value="合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="1")
    private String contractCode;

    /**
     * 客户ID
     */
    @Column(name = "supplier_id")
    @ApiModelProperty(name="supplierId" ,value="客户id")
    private Long supplierId;

    /**
     * 产品料号ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name="materialId" ,value="产品料号id")
    private Long materialId;

    /**
     * 订单数量
     */
    @Column(name = "order_quantity")
    @ApiModelProperty(name="orderQuantity" ,value="订单数量")
    @Excel(name = "订单数量", height = 20, width = 30,orderNum="6")
    private Integer orderQuantity;

    /**
     * 已交付数量
     */
    @Column(name = "delivered_quantity")
    @ApiModelProperty(name="deliveredQuantity" ,value="已交付数量")
    @Excel(name = "已交付数量", height = 20, width = 30,orderNum="7")
    private Integer deliveredQuantity;

    /**
     * 喷绘信息
     */
    @Column(name = "inkjet_paint")
    @ApiModelProperty(name="inkjetPaint" ,value="喷绘信息")
    @Excel(name = "喷绘信息", height = 20, width = 30,orderNum="8")
    private String inkjetPaint;

    /**
     * 镭雕信息
     */
    @Column(name = "radium_carving")
    @ApiModelProperty(name="radiumCarving" ,value="镭雕信息")
    @Excel(name = "镭雕信息", height = 20, width = 30,orderNum="9")
    private String radiumCarving;

    /**
     * 排产状态（0、待排产 1、排产中 2、排产完成 3、订单完工）
     */
    @ApiModelProperty(name="schedule_status" ,value="排产状态（0、待排产 1、排产中 2、排产完成 3、订单完工）")
    @Excel(name = "排产状态", height = 20, width = 30,orderNum="10",replace = {"待排产_0","排产中_1","排产完成_2","订单完工_3"})
    private Byte scheduleStatus;

    /**
     * 状态（0、创建 1、下载完成 2、已完工 3、做废）
     */
    @ApiModelProperty(name="status" ,value="状态（0、创建 1、下载完成 2、已完工 3、做废）")
    @Excel(name = "状态", height = 20, width = 30,orderNum="10",replace = {"创建_0","下载完成_1","已完工_2","作废_3"})
    private Byte status;

    /**
     * 交货日期
     */
    @Column(name = "delivery_date")
    @ApiModelProperty(name="deliveryDate" ,value="交货日期")
    @Excel(name = "交货日期", height = 20, width = 30,orderNum="11")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    /**
     * 排产交期
     */
    @Column(name = "schedule_date")
    @ApiModelProperty(name="schedule_date" ,value="排产交期")
    @Excel(name = "排产交期", height = 20, width = 30,orderNum="11")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    /**
     * 下单日期
     */
    @Column(name = "order_date")
    @ApiModelProperty(name="order_date" ,value="下单日期")
    @Excel(name = "下单日期", height = 20, width = 30,orderNum="11")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    /**
     * 业务员名称
     */
    @Column(name = "sales_man_name")
    @ApiModelProperty(name="salesManName" ,value="业务员名称")
    private String salesManName;

    /**
     * 照片链接
     */
    @Column(name = "photo_url")
    @ApiModelProperty(name="photoUrl" ,value="照片链接")
    private String photoUrl;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;


    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "镭雕信息", height = 20, width = 30,orderNum="13")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="15")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;

    /**
     * 客货号
     */
    @Column(name = "freight_num")
    @ApiModelProperty(name="freightNum" ,value="客货号")
    private String freightNum;

    /**
     * 合同交期
     */
    @Column(name = "contract_date")
    @ApiModelProperty(name="contractDate" ,value="合同交期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractDate;
}
