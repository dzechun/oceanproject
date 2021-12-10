package com.fantechs.common.base.general.entity.wms.in;

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
 * 入库计划单明细
 * wms_in_in_plan_order_det
 * @author 81947
 * @date 2021-12-08 10:13:51
 */
@Data
@Table(name = "wms_in_in_plan_order_det")
public class WmsInInPlanOrderDet extends ValidGroup implements Serializable {
    /**
     * 入库计划单明细ID
     */
    @ApiModelProperty(name="inPlanOrderDetId",value = "入库计划单明细ID")
    @Excel(name = "入库计划单明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "in_plan_order_det_id")
    private Long inPlanOrderDetId;

    /**
     * 入库计划单ID
     */
    @ApiModelProperty(name="inPlanOrderId",value = "入库计划单ID")
    @Excel(name = "入库计划单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "in_plan_order_id")
    private Long inPlanOrderId;

    /**
     * 核心单编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单编码")
    @Excel(name = "核心单编码", height = 20, width = 30,orderNum="") 
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
     * 行号
     */
    @ApiModelProperty(name="lineNumber",value = "行号")
    @Excel(name = "行号", height = 20, width = 30,orderNum="") 
    @Column(name = "line_number")
    private String lineNumber;

    /**
     * 来源ID
     */
    @ApiModelProperty(name="sourceId",value = "来源ID")
    @Excel(name = "来源ID", height = 20, width = 30,orderNum="") 
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_qty")
    private BigDecimal planQty;

    /**
     * 上架数量
     */
    @ApiModelProperty(name="putawayQty",value = "上架数量")
    @Excel(name = "上架数量", height = 20, width = 30,orderNum="") 
    @Column(name = "putaway_qty")
    private BigDecimal putawayQty;

    /**
     * 库存状态id
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态id")
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    /**
     * 行状态(1-待作业 2-作业中 3-完成)
     */
    @ApiModelProperty(name="lineStatus",value = "行状态(1-待作业 2-作业中 3-完成)")
    @Excel(name = "行状态(1-待作业 2-作业中 3-完成)", height = 20, width = 30,orderNum="") 
    @Column(name = "line_status")
    private Byte lineStatus;

    /**
     * 生产日期(生产时间)
     */
    @ApiModelProperty(name="productionTime",value = "生产日期(生产时间)")
    @Excel(name = "生产日期(生产时间)", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "production_time")
    private Date productionTime;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}