package com.fantechs.common.base.general.entity.mes.pm;

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

/**
 * 生产管理-工单表
 * mes_pm_work_order
 * @author hyc
 * @date 2021-04-09 14:44:55
 */
@Data
@Table(name = "mes_pm_work_order")
public class MesPmWorkOrder extends ValidGroup implements Serializable {
    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Id
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工单编码
     */
    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    @Excel(name = "工单编码", height = 20, width = 30)
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 父工单ID
     */
    @ApiModelProperty(name="parentId",value = "父工单ID")
    @Excel(name = "父工单ID", height = 20, width = 30)
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 销售订单ID
     */
    @ApiModelProperty(name="salesOrderId",value = "销售订单ID")
    @Excel(name = "销售订单ID", height = 20, width = 30)
    @Column(name = "sales_order_id")
    private Long salesOrderId;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId",value = "产品料号ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工单数量
     */
    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    @Excel(name = "工单数量", height = 20, width = 30)
    @Column(name = "work_order_qty")
    private BigDecimal workOrderQty;

    /**
     * 投产数量
     */
    @ApiModelProperty(name="productionQty",value = "投产数量")
    @Excel(name = "投产数量", height = 20, width = 30)
    @Column(name = "production_qty")
    private BigDecimal productionQty;

    /**
     * 完工数量
     */
    @ApiModelProperty(name="outputQty",value = "完工数量")
    @Excel(name = "完工数量", height = 20, width = 30)
    @Column(name = "output_qty")
    private BigDecimal outputQty;

    /**
     * 累计下发数量
     */
    @ApiModelProperty(name="totalIssueQty",value = "累计下发数量")
    @Excel(name = "累计下发数量", height = 20, width = 30)
    @Column(name = "total_issue_qty")
    private BigDecimal totalIssueQty;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    @Excel(name = "是否已全部下发(0-否 1-是)", height = 20, width = 30)
    @Column(name = "if_all_issued")
    private Byte ifAllIssued;

    /**
     * 当前排产数量
     */
    @ApiModelProperty(name="scheduledQty",value = "当前排产数量")
    @Excel(name = "当前排产数量", height = 20, width = 30)
    @Column(name = "scheduled_qty")
    private BigDecimal scheduledQty;

    /**
     * 工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)
     */
    @ApiModelProperty(name="workOrderStatus",value = "工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)")
    @Excel(name = "工单状态", height = 20, width = 30,replace = {"Initial_1","Release_2","WIP_3","Hold_4","Cancel_5","Complete_6","Delete_7"})
    @Column(name = "work_order_status")
    private Byte workOrderStatus;

    /**
     * 线别ID
     */
    @ApiModelProperty(name="proLineId",value = "线别ID")
    @Excel(name = "线别ID", height = 20, width = 30)
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId",value = "条码规则集合ID")
    @Column(name = "barcode_rule_set_id")
    private Long barcodeRuleSetId;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @ApiModelProperty(name="workOrderType",value = "工单类型(0、量产 1、试产 2、返工 3、维修)")
    @Excel(name = "工单类型", height = 20, width = 30,replace = {"量产_0","试产_1","返工_2","维修_3"})
    @Column(name = "work_order_type")
    private Byte workOrderType;

    /**
     * 排产日期
     */
    @ApiModelProperty(name="scheduleDate",value = "排产日期")
    @Excel(name = "排产日期", height = 20, width = 30)
    @Column(name = "schedule_date")
    private Date scheduleDate;

    /**
     * 计划开始时间
     */
    @ApiModelProperty(name="planStartTime",value = "计划开始时间")
    @Excel(name = "计划开始时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_start_time")
    private Date planStartTime;

    /**
     * 计划结束时间
     */
    @ApiModelProperty(name="planEndTime",value = "计划结束时间")
    @Excel(name = "计划结束时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_end_time")
    private Date planEndTime;

    /**
     * 实际开始时间
     */
    @ApiModelProperty(name="actualStartTime",value = "实际开始时间")
    @Excel(name = "实际开始时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_start_time")
    private Date actualStartTime;

    /**
     * 实际结束时间
     */
    @ApiModelProperty(name="actualEndTime",value = "实际结束时间")
    @Excel(name = "实际结束时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_end_time")
    private Date actualEndTime;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractNo",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30)
    @Column(name = "contract_no")
    private String contractNo;

    /**
     *  投产工序ID
     */
    @ApiModelProperty(name="putIntoProcessId",value = "投产工序ID")
    @Column(name = "put_into_process_id")
    private Long putIntoProcessId;

    /**
     *  产出工序ID
     */
    @ApiModelProperty(name="outputProcessId",value = "产出工序ID")
    @Column(name = "output_process_id")
    private Long outputProcessId;

    /**
     *  报废数量
     */
    @ApiModelProperty(name="scrapQty",value = "报废数量")
    @Column(name = "scrap_qty")
    private BigDecimal scrapQty;

    /**
     *  入库数量
     */
    @ApiModelProperty(name="inventoryQty",value = "入库数量")
    @Column(name = "inventory_qty")
    private BigDecimal inventoryQty;

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
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;


    /**
     *  批次号
     */
    @ApiModelProperty(name="batchCode",value = "入库数量")
    @Column(name = "batch_code")
    private String batchCode;

    @Transient
    @ApiModelProperty("下发数量")
    private BigDecimal qty;

    /**
     * 仓库ID
     */
    @Transient
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;
}
