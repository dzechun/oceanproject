package com.fantechs.common.base.entity.apply;

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

@Table(name = "smt_work_order")
@Data
public class SmtWorkOrder extends ValidGroup implements Serializable {
    private static final long serialVersionUID = -6958409644458668492L;
    /**
     * 工单ID
     */
    @Id
    @Column(name = "work_order_id")
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    @NotNull(groups = update.class,message = "工单ID不能为空")
    private Long workOrderId;

    /**
     * 工单号
     */
    @Column(name = "work_order_code")
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @NotBlank(message = "工单号不能为空")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="1")
    private String workOrderCode;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    @ApiModelProperty(name="orderId" ,value="订单ID")
    private Long orderId;

    /**
     * 产品料号ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    @NotNull(message = "产品料号ID不能为空")
    private Long materialId;

    /**
     * 工单数量
     */
    @Column(name = "work_order_quantity")
    @ApiModelProperty(name="workOrderQuantity" ,value="工单数量")
    @NotNull(message = "工单数量不能为空")
    @Excel(name = "工单数量", height = 20, width = 30,orderNum="5")
    private java.math.BigDecimal workOrderQuantity;

    /**
     * 投产数量
     */
    @Column(name = "production_quantity")
    @ApiModelProperty(name="productionQuantity" ,value="投产数量")
    @Excel(name = "投产数量", height = 20, width = 30,orderNum="6")
    private java.math.BigDecimal productionQuantity;

    /**
     * 完工数量
     */
    @Column(name = "output_quantity")
    @ApiModelProperty(name="outputQuantity" ,value="完工数量")
    @Excel(name = "完工数量", height = 20, width = 30,orderNum="7")
    private java.math.BigDecimal outputQuantity;

    /**
     * 排产数量
     */
    @Column(name = "scheduled_quantity")
    @ApiModelProperty(name="scheduledQuantity" ,value="排产数量")
    @Excel(name = "排产数量", height = 20, width = 30,orderNum="7")
    private java.math.BigDecimal scheduledQuantity;

    /**
     * 工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
     */
    @Column(name = "work_order_status")
    @ApiModelProperty(name="workOrderStatus" ,value="工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)")
    @Excel(name = "工单状态", height = 20, width = 30 ,orderNum="8",replace = {"待生产_0", "生产中_1","暂停生产_2","生产完成_3"})
    private Integer workOrderStatus;

    /**
     * 线别ID
     */
    @Column(name = "pro_line_id")
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    @NotNull(message = "线别ID不能为空")
    private Long proLineId;

    /**
     * 工艺路线ID
     */
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    @NotNull(message = "工艺路线ID不能为空")
    private Long routeId;

    /**
     * 条码规则集合ID
     */
    @Column(name = "barcode_rule_set_id")
    @ApiModelProperty(name="barcodeRuleSetId" ,value="条码规则集合ID")
    private Long barcodeRuleSetId;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @Column(name = "work_order_type")
    @ApiModelProperty(name="workOrderType" ,value="工单类型")
    @Excel(name = "工单类型(0、量产 1、试产 2、返工 3、维修)", height = 20, width = 30,orderNum="12",replace = {"量产_0", "试产_1","返工_2","维修_3"})
    private Integer workOrderType;

    /**
     * 计划开始时间
     */
    @Column(name = "planned_start_time")
    @ApiModelProperty(name="plannedStartTime" ,value="计划开始时间")
    @Excel(name = "计划开始时间", height = 20, width = 30,orderNum="13",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "计划开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plannedStartTime;

    /**
     * 计划结束时间
     */
    @Column(name = "planned_end_time")
    @ApiModelProperty(name="plannedEndTime" ,value="计划结束时间")
    @Excel(name = "计划结束时间", height = 20, width = 30,orderNum="14",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plannedEndTime;

    /**
     * 实际开始时间
     */
    @Column(name = "actual_start_time")
    @ApiModelProperty(name="actualStartTime" ,value="实际开始时间")
    @Excel(name = "实际开始时间", height = 20, width = 30,orderNum="15",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date actualStartTime;

    /**
     * 实际结束时间
     */
    @Column(name = "actual_end_time")
    @ApiModelProperty(name="actualEndTime" ,value="实际结束时间")
    @Excel(name = "实际结束时间", height = 20, width = 30,orderNum="16",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date actualEndTime;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractNo",value = "合同号")
    @Column(name = "contract_no")
    private String contractNo;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="18",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="20",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
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

}
