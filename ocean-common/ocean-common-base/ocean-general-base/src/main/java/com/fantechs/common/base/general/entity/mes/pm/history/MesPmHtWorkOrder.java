package com.fantechs.common.base.general.entity.mes.pm.history;

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
 * 生产管理-工单履历表
 * mes_pm_ht_work_order
 * @author hyc
 * @date 2021-04-09 15:29:27
 */
@Data
@Table(name = "mes_pm_ht_work_order")
public class MesPmHtWorkOrder extends ValidGroup implements Serializable {
    /**
     * 工单履历表ID
     */
    @ApiModelProperty(name="htWorkOrderId",value = "工单履历表ID")
    @Excel(name = "工单履历表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_work_order_id")
    private Long htWorkOrderId;

    /**
     * 工单表ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单表ID")
    @Excel(name = "工单表ID", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工单编码
     */
    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    @Excel(name = "工单编码", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 父工单ID
     */
    @ApiModelProperty(name="parentId",value = "父工单ID")
    @Excel(name = "父工单ID", height = 20, width = 30,orderNum="")
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
    @Excel(name = "产品料号ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工单数量
     */
    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    @Excel(name = "工单数量", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_qty")
    private BigDecimal workOrderQty;

    /**
     * 投产数量
     */
    @ApiModelProperty(name="productionQty",value = "投产数量")
    @Excel(name = "投产数量", height = 20, width = 30,orderNum="")
    @Column(name = "production_qty")
    private BigDecimal productionQty;

    /**
     * 完工数量
     */
    @ApiModelProperty(name="outputQty",value = "完工数量")
    @Excel(name = "完工数量", height = 20, width = 30,orderNum="")
    @Column(name = "output_qty")
    private BigDecimal outputQty;

    /**
     * 当前排产数量
     */
    @ApiModelProperty(name="scheduledQty",value = "当前排产数量")
    @Excel(name = "当前排产数量", height = 20, width = 30,orderNum="")
    @Column(name = "scheduled_qty")
    private BigDecimal scheduledQty;

    /**
     * 工单状态(0、待生产 1、待首检 2、生产中 3、暂停生产 4、生产完成 5、工单挂起)
     */
    @ApiModelProperty(name="workOrderStatus",value = "工单状态(0、待生产 1、待首检 2、生产中 3、暂停生产 4、生产完成 5、工单挂起)")
    @Excel(name = "工单状态(0、待生产 1、待首检 2、生产中 3、暂停生产 4、生产完成 5、工单挂起)", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_status")
    private Byte workOrderStatus;

    /**
     * 线别ID
     */
    @ApiModelProperty(name="proLineId",value = "线别ID")
    @Excel(name = "线别ID", height = 20, width = 30,orderNum="")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    @Excel(name = "工艺路线ID", height = 20, width = 30,orderNum="")
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId",value = "条码规则集合ID")
    @Excel(name = "条码规则集合ID", height = 20, width = 30,orderNum="")
    @Column(name = "barcode_rule_set_id")
    private Long barcodeRuleSetId;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @ApiModelProperty(name="workOrderType",value = "工单类型(0、量产 1、试产 2、返工 3、维修)")
    @Excel(name = "工单类型(0、量产 1、试产 2、返工 3、维修)", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_type")
    private Byte workOrderType;

    /**
     * 排产日期
     */
    @ApiModelProperty(name="scheduleDate",value = "排产日期")
    @Excel(name = "排产日期", height = 20, width = 30,orderNum="")
    @Column(name = "schedule_date")
    private Date scheduleDate;

    /**
     * 计划开始时间
     */
    @ApiModelProperty(name="planStartTime",value = "计划开始时间")
    @Excel(name = "计划开始时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_start_time")
    private Date planStartTime;

    /**
     * 计划结束时间
     */
    @ApiModelProperty(name="planEndTime",value = "计划结束时间")
    @Excel(name = "计划结束时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_end_time")
    private Date planEndTime;

    /**
     * 实际开始时间
     */
    @ApiModelProperty(name="actualStartTime",value = "实际开始时间")
    @Excel(name = "实际开始时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_start_time")
    private Date actualStartTime;

    /**
     * 实际结束时间
     */
    @ApiModelProperty(name="actualEndTime",value = "实际结束时间")
    @Excel(name = "实际结束时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_end_time")
    private Date actualEndTime;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractNo",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="")
    @Column(name = "contract_no")
    private String contractNo;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;


    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="物料版本")
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 条码规则集合ID
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleName" ,value="条码规则集合")
    private String barcodeRuleName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;
}
