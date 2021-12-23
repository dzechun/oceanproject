package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 备料计划单明细
 * wms_out_plan_stock_list_order_det
 * @author Dylan
 * @date 2021-12-22 19:17:47
 */
@Data
@Table(name = "wms_out_plan_stock_list_order_det")
public class WmsOutPlanStockListOrderDet extends ValidGroup implements Serializable {
    /**
     * 备料计划单明细ID
     */
    @ApiModelProperty(name="planStockListOrderDetId",value = "备料计划单明细ID")
    @Excel(name = "备料计划单明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "plan_stock_list_order_det_id")
    private Long planStockListOrderDetId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单据编码")
    @Excel(name = "核心单据编码", height = 20, width = 30,orderNum="") 
    @Column(name = "core_source_order_code")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据编码")
    @Excel(name = "来源单据编码", height = 20, width = 30,orderNum="") 
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 核心来源ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心来源ID")
    @Excel(name = "核心来源ID", height = 20, width = 30,orderNum="") 
    @Column(name = "core_source_id")
    private Long coreSourceId;

    /**
     * 来源ID
     */
    @ApiModelProperty(name="sourceId",value = "来源ID")
    @Excel(name = "来源ID", height = 20, width = 30,orderNum="") 
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 备料计划单ID
     */
    @ApiModelProperty(name="planStockListOrderId",value = "备料计划单ID")
    @Excel(name = "备料计划单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_stock_list_order_id")
    private Long planStockListOrderId;

    /**
     * 生产订单ID
     */
    @ApiModelProperty(name="workOrderId",value = "生产订单ID")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQty",value = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30,orderNum="") 
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    /**
     * 实际拣货数量
     */
    @ApiModelProperty(name="actualQty",value = "实际拣货数量")
    @Excel(name = "实际拣货数量", height = 20, width = 30,orderNum="") 
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 行状态(1-待出库 2-出库中 3-出库完成)
     */
    @ApiModelProperty(name="lineStatus",value = "行状态(1-待出库 2-出库中 3-出库完成)")
    @Excel(name = "行状态(1-待出库 2-出库中 3-出库完成)", height = 20, width = 30,orderNum="") 
    @Column(name = "line_status")
    private Byte lineStatus;

    /**
     * 出库用户ID
     */
    @ApiModelProperty(name="deliveryUserId",value = "出库用户ID")
    @Excel(name = "出库用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_user_id")
    private Long deliveryUserId;

    /**
     * 累计下发数量
     */
    @ApiModelProperty(name="totalIssueQty",value = "累计下发数量")
    @Excel(name = "累计下发数量", height = 20, width = 30,orderNum="") 
    @Column(name = "total_issue_qty")
    private BigDecimal totalIssueQty;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    @Excel(name = "是否已全部下发(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "if_all_issued")
    private Byte ifAllIssued;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private String remark;

    /**
     * 组织ID
     */
    @ApiModelProperty(name="orgId",value = "组织ID")
    @Excel(name = "组织ID", height = 20, width = 30,orderNum="") 
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
     * 拓展字段1
     */
    @ApiModelProperty(name="option1",value = "拓展字段1")
    @Excel(name = "拓展字段1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * 拓展字段2
     */
    @ApiModelProperty(name="option2",value = "拓展字段2")
    @Excel(name = "拓展字段2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * 拓展字段3
     */
    @ApiModelProperty(name="option3",value = "拓展字段3")
    @Excel(name = "拓展字段3", height = 20, width = 30,orderNum="") 
    private String option3;

    private static final long serialVersionUID = 1L;
}