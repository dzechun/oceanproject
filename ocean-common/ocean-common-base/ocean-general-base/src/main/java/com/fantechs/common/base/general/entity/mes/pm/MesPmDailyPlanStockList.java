package com.fantechs.common.base.general.entity.mes.pm;

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
 * 日计划备料表
 * mes_pm_daily_plan_stock_list
 * @author Dylan
 * @date 2021-12-21 13:54:19
 */
@Data
@Table(name = "mes_pm_daily_plan_stock_list")
public class MesPmDailyPlanStockList extends ValidGroup implements Serializable {
    /**
     * 日计划备料表ID
     */
    @ApiModelProperty(name="dailyPlanStockListId",value = "日计划备料表ID")
    @Excel(name = "日计划备料表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "daily_plan_stock_list_id")
    private Long dailyPlanStockListId;

    /**
     * 日计划明细表ID
     */
    @ApiModelProperty(name="dailyPlanDetId",value = "日计划明细表ID")
    @Excel(name = "日计划明细表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "daily_plan_det_id")
    private Long dailyPlanDetId;

    /**
     * 零件物料ID
     */
    @ApiModelProperty(name="partMaterialId",value = "零件物料ID")
    @Excel(name = "零件物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "part_material_id")
    private Long partMaterialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_id")
    private Long processId;

    /**
     * 单个用量
     */
    @ApiModelProperty(name="singleQty",value = "单个用量")
    @Excel(name = "单个用量", height = 20, width = 30,orderNum="") 
    @Column(name = "single_qty")
    private BigDecimal singleQty;

    /**
     * 日计划使用数量
     */
    @ApiModelProperty(name="dailyPlanUsageQty",value = "日计划使用数量")
    @Excel(name = "日计划使用数量", height = 20, width = 30,orderNum="") 
    @Column(name = "daily_plan_usage_qty")
    private BigDecimal dailyPlanUsageQty;

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