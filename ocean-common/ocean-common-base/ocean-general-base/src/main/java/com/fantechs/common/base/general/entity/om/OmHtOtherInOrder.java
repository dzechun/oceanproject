package com.fantechs.common.base.general.entity.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 其他入库订单履历表
 * om_ht_other_in_order
 * @author mr.lei
 * @date 2021-06-21 17:40:47
 */
@Data
@Table(name = "om_ht_other_in_order")
public class OmHtOtherInOrder extends ValidGroup implements Serializable {
    /**
     * 其他入库订单履历表ID
     */
    @ApiModelProperty(name="htOtherInOrderId",value = "其他入库订单履历表ID")
    @Excel(name = "其他入库订单履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_other_in_order_id")
    private Long htOtherInOrderId;

    /**
     * 其他入库订单ID
     */
    @ApiModelProperty(name="otherInOrderId",value = "其他入库订单ID")
    @Excel(name = "其他入库订单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "other_in_order_id")
    private Long otherInOrderId;

    /**
     * 其他入库订单单号
     */
    @ApiModelProperty(name="otherInOrderCode",value = "其他入库订单单号")
    @Excel(name = "其他入库订单单号", height = 20, width = 30,orderNum="") 
    @Column(name = "other_in_order_code")
    private String otherInOrderCode;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="") 
    @Column(name = "related_order_code")
    private String relatedOrderCode;

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
    @ApiModelProperty(name="totalIssueQty",value = "总加入数量")
    @Excel(name = "总加入数量", height = 20, width = 30,orderNum="") 
    @Column(name = "total_issue_qty")
    private BigDecimal totalIssueQty;

    /**
     * 总收货数量
     */
    @ApiModelProperty(name="totalReceivingQty",value = "总收货数量")
    @Excel(name = "总收货数量", height = 20, width = 30,orderNum="") 
    @Column(name = "total_receiving_qty")
    private BigDecimal totalReceivingQty;

    /**
     * 订单总数量
     */
    @ApiModelProperty(name="totalQty",value = "订单总数量")
    @Excel(name = "订单总数量", height = 20, width = 30,orderNum="") 
    @Column(name = "total_qty")
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
    @ApiModelProperty(name="planCompleteTime",value = "要求完成日期")
    @Excel(name = "要求完成日期", height = 20, width = 30,orderNum="")
    @Column(name = "plan_complete_time")
    private Date planCompleteTime;

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

    private static final long serialVersionUID = 1L;
}