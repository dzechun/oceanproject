package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulkDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessCard;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Mr.Lei
 * @create 2021/1/19
 */
@Data
public class MesPmProcessCardDto implements Serializable {

    /**
     * 工单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    @Column(name = "work_order_id")
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    @NotNull(groups = ValidGroup.update.class,message = "工单ID不能为空")
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
    private BigDecimal workOrderQuantity;

    /**
     * 投产数量
     */
    @Column(name = "production_quantity")
    @ApiModelProperty(name="productionQuantity" ,value="投产数量")
    @Excel(name = "投产数量", height = 20, width = 30,orderNum="6")
    private BigDecimal productionQuantity;

    /**
     * 完工数量
     */
    @Column(name = "output_quantity")
    @ApiModelProperty(name="outputQuantity" ,value="完工数量")
    @Excel(name = "完工数量", height = 20, width = 30,orderNum="7")
    private BigDecimal outputQuantity;

    /**
     * 排产数量
     */
    @Column(name = "scheduled_quantity")
    @ApiModelProperty(name="scheduledQuantity" ,value="排产数量")
    @Excel(name = "排产数量", height = 20, width = 30,orderNum="7")
    private BigDecimal scheduledQuantity;

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
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 条码规则名称
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleSetName" ,value="条码规则名称")
    private String barcodeRuleSetName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30,orderNum="3")
    private String version;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 转移批量
     */
    @Transient
    @ApiModelProperty(name="transferQuantity" ,value="转移批量")
    private Integer transferQuantity;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "生产线", height = 20, width = 30,orderNum = "9")
    private String proName;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="10")
    private String routeName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="17")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="19")
    private String modifiedUserName;

    /**
     * 投入工序
     */
    @Transient
    @ApiModelProperty(name="putIntoProcessName" ,value="投入工序")
    private String putIntoProcessName;

    /**
     * 产出工序
     */
    @Transient
    @ApiModelProperty(name="productionProcessName" ,value="产出工序")
    private String productionProcessName;

    /**
     * 流转卡规则解析码
     */
    @Transient
    @ApiModelProperty(name="analysisCode" ,value="流转卡规则解析码")
    private String analysisCode;

    /**
     * 订单号
     */
    @Transient
    @ApiModelProperty(name="orderCode" ,value="订单号")
    private String orderCode;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 包装单位-ID
     */
    @Transient
    @ApiModelProperty(name = "packingUnitId",value = "包装单位-ID")
    private Long packingUnitId;

    /**
     * 包装单位-名称
     */
    @Transient
    @ApiModelProperty(name = "packingUnitName",value = "包装单位-名称")
    private String packingUnitName;

    /**
     * 包装规格-ID
     */
    @Transient
    @ApiModelProperty(name = "packageSpecificationId",value = "包装规格-ID")
    private Long packageSpecificationId;

    /**
     * 包装规格-数量
     */
    @Transient
    @ApiModelProperty(name = "packageSpecificationQuantity",value = "包装规格-数量")
    private String packageSpecificationQuantity;

    /**
     * 产品颜色
     */
    @Transient
    @ApiModelProperty(name = "color",value = "产品颜色")
    private String color;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModuleName",value = "产品型号")
    private String productModuleName;


    /**
     * 工序过站信息
     */
    @ApiModelProperty(name = "smtProcessListProcessDtos",value = "工序过站集合")
    private List<SmtProcessListProcessDto> smtProcessListProcessDtos;

    @ApiModelProperty(name = "mesPmBreakBulkDets",value = "拆/合批集合")
    private List<MesPmBreakBulkDet> mesPmBreakBulkDets;
}
