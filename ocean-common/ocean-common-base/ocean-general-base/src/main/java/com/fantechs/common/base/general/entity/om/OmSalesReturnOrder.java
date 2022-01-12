package com.fantechs.common.base.general.entity.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

;
;

/**
 * 销退入库单
 * om_sales_return_order
 * @author mr.lei
 * @date 2021-06-21 14:12:58
 */
@Data
@Table(name = "om_sales_return_order")
public class OmSalesReturnOrder extends ValidGroup implements Serializable {
    /**
     * 销退入库单ID
     */
    @ApiModelProperty(name="salesReturnOrderId",value = "销退入库单ID")
    @Id
    @Column(name = "sales_return_order_id")
    private Long salesReturnOrderId;

    /**
     * 销退入库单号
     */
    @ApiModelProperty(name="salesReturnOrderCode",value = "销退入库单号")
    @Excel(name = "销退入库单号", height = 20, width = 30,orderNum="1")
    @Column(name = "sales_return_order_code")
    private String salesReturnOrderCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    @Column(name = "source_big_type")
    private Byte sourceBigType;

    /**
     * 总加入数量
     */
    @Transient
    @ApiModelProperty(name="totalIssueQty",value = "总加入数量")
    @Excel(name = "累计下发数量", height = 20, width = 30,orderNum="6")
    private BigDecimal totalIssueQty;

    /**
     * 总收货数量
     */
    @Transient
    @ApiModelProperty(name="totalReceivingQty",value = "总收货数量")
    @Excel(name = "实收数量", height = 20, width = 30,orderNum="7")
    private BigDecimal totalReceivingQty;

    /**
     * 订单总数量
     */
    @Transient
    @ApiModelProperty(name="totalQty",value = "订单总数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="5")
    private BigDecimal totalQty;

    /**
     * 订单总体积
     */
    @ApiModelProperty(name="totalVolume",value = "订单总体积")
    @Excel(name = "体积", height = 20, width = 30,orderNum="8")
    @Transient
    private BigDecimal totalVolume;

    /**
     * 订单总净重
     */
    @ApiModelProperty(name="totalNetWeight",value = "订单总净重")
    @Excel(name = "净重", height = 20, width = 30,orderNum="9")
    @Transient
    private BigDecimal totalNetWeight;

    /**
     * 订单总毛重
     */
    @ApiModelProperty(name="totalGrossWeight",value = "订单总毛重")
    @Excel(name = "毛重", height = 20, width = 30,orderNum="10")
    @Transient
    private BigDecimal totalGrossWeight;

    /**
     * 订单状态(1-打开 2-下发中  3-已下发 4-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-打开 2-下发中 3-已下发 4-完成)")
    @Excel(name = "订单状态(1-打开 2-下发中  3-已下发 4-完成)", height = 20, width = 30,orderNum="2")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="4",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "order_date")
    private Date orderDate;

    /**
     * 要求完成日期
     */
    @ApiModelProperty(name="completeDate",value = "要求完成日期")
    @Excel(name = "要求完成日期", height = 20, width = 30,orderNum="3",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "complete_date")
    @JSONField(format = "yyyy-MM-dd")
    private Date completeDate;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="11")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    @ApiModelProperty("明细")
    @Transient
    private List<OmSalesReturnOrderDet> omSalesReturnOrderDets;

    private static final long serialVersionUID = 1L;
}