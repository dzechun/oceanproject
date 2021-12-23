package com.fantechs.common.base.general.entity.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesPmDailyPlanDet extends BaseQuery implements Serializable {

    /**
     * 日计划明细ID
     */
    @ApiModelProperty(name="dailyPlanDetId",value = "日计划明细ID")
    private Long dailyPlanDetId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单据编码")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据编码")
    private String sourceOrderCode;

    /**
     * 核心来源ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心来源ID")
    private Long coreSourceId;

    /**
     * 来源ID
     */
    @ApiModelProperty(name="sourceId",value = "来源ID")
    private Long sourceId;

    /**
     * 日计划ID
     */
    @ApiModelProperty(name="dailyPlanId",value = "日计划ID")
    private Long dailyPlanId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 排产数量
     */
    @ApiModelProperty(name="scheduleQty",value = "排产数量")
    private BigDecimal scheduleQty;

    /**
     * 完成数量
     */
    @ApiModelProperty(name="finishedQty",value = "完成数量")
    private BigDecimal finishedQty;

    /**
     * 计划开始时间
     */
    @ApiModelProperty(name="planStartTime",value = "计划开始时间")
    private Date planStartTime;

    /**
     * 是否插单(0-否 1-是)
     */
    @ApiModelProperty(name="ifOrderInserting",value = "是否插单(0-否 1-是)")
    private Byte ifOrderInserting;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;


}
