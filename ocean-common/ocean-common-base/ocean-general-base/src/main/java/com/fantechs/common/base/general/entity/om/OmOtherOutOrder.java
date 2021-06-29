package com.fantechs.common.base.general.entity.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

/**
 * 其他出库订单
 * om_other_out_order
 * @author mr.lei
 * @date 2021-06-23 13:59:37
 */
@Data
@Table(name = "om_other_out_order")
public class OmOtherOutOrder extends ValidGroup implements Serializable {
    /**
     * 其他出库订单ID
     */
    @ApiModelProperty(name="otherOutOrderId",value = "其他出库订单ID")
    @Excel(name = "其他出库订单ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "other_out_order_id")
    private Long otherOutOrderId;

    /**
     * 其他出库订单单号
     */
    @ApiModelProperty(name="otherOutOrderCode",value = "其他出库订单单号")
    @Excel(name = "其他出库订单单号", height = 20, width = 30,orderNum="") 
    @Column(name = "other_out_order_code")
    private String otherOutOrderCode;

    /**
     * 客户订单号
     */
    @ApiModelProperty(name="customerOrderCode",value = "客户订单号")
    @Excel(name = "客户订单号", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_order_code")
    private String customerOrderCode;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Excel(name = "货主ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 总加入数量
     */
    @Transient
    @ApiModelProperty(name="totalIssueQty",value = "总加入数量")
    @Excel(name = "总加入数量", height = 20, width = 30,orderNum="")
    private BigDecimal totalIssueQty;

    /**
     * 累计发货数量
     */
    @Transient
    @ApiModelProperty(name="totalDispatchQty",value = "累计发货数量")
    @Excel(name = "累计发货数量", height = 20, width = 30,orderNum="")
    private BigDecimal totalDispatchQty;

    /**
     * 收货人ID
     */
    @ApiModelProperty(name="consigneeId",value = "收货人ID")
    @Excel(name = "收货人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "consignee_id")
    private Long consigneeId;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    @Excel(name = "联系人名称", height = 20, width = 30,orderNum="") 
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系人电话")
    @Excel(name = "联系电话", height = 20, width = 30,orderNum="")
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 传真
     */
    @ApiModelProperty(name="faxNumber",value = "传真")
    @Excel(name = "传真", height = 20, width = 30,orderNum="")
    @Column(name = "fax_number")
    private String faxNumber;

    /**
     * 邮件地址
     */
    @ApiModelProperty(name="eMailAddress",value = "邮件地址")
    @Excel(name = "邮件地址", height = 20, width = 30,orderNum="")
    @Column(name = "e_mail_address")
    private String eMailAddress;

    /**
     * 地址
     */
    @ApiModelProperty(name="address",value = "地址")
    @Excel(name = "地址", height = 20, width = 30,orderNum="")
    @Column(name = "address")
    private String address;

    /**
     * 订单总数量
     */
    @Transient
    @ApiModelProperty(name="totalQty",value = "订单总数量")
    @Excel(name = "订单总数量", height = 20, width = 30,orderNum="")
    private BigDecimal totalQty;

    /**
     * 订单总体积
     */
    @ApiModelProperty(name="totalVolume",value = "订单总体积")
    @Excel(name = "订单总体积", height = 20, width = 30,orderNum="") 
    @Column(name = "total_volume")
    private BigDecimal totalVolume;

    /**
     * 订单总净重
     */
    @ApiModelProperty(name="totalNetWeight",value = "订单总净重")
    @Excel(name = "订单总净重", height = 20, width = 30,orderNum="") 
    @Column(name = "total_net_weight")
    private BigDecimal totalNetWeight;

    /**
     * 订单总毛重
     */
    @ApiModelProperty(name="totalGrossWeight",value = "订单总毛重")
    @Excel(name = "订单总毛重", height = 20, width = 30,orderNum="") 
    @Column(name = "total_gross_weight")
    private BigDecimal totalGrossWeight;

    /**
     * 订单状态(1-打开 2-下发中  3-已下发 4-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-打开 2-下发中  3-已下发 4-完成)")
    @Excel(name = "订单状态(1-打开 2-下发中  3-已下发 4-完成)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="") 
    @Column(name = "order_date")
    private Date orderDate;

    /**
     * 要求完成日期
     */
    @ApiModelProperty(name="completeDate",value = "要求完成日期")
    @Excel(name = "要求完成日期", height = 20, width = 30,orderNum="14")
    @Column(name = "complete_date")
    private Date completeDate;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="") 
    private String option3;

    @Transient
    private List<OmOtherOutOrderDet> omOtherOutOrderDets;

    private static final long serialVersionUID = 1L;
}