package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesPmDailyPlanDto extends MesPmDailyPlan implements Serializable {

/*    *//**
     * 计划日期
     *//*
    @Transient
    @ApiModelProperty(name = "planDate",value = "计划日期（yyyy-MM-dd）")
    private String planDate;*/

    /**
     * 生产数量
     */
    @Transient
    @ApiModelProperty(name="workOrderQty",value = "生产数量")
    private BigDecimal workOrderQty;


    /**
     * 未排产数量
     */
    @Transient
    @ApiModelProperty(name="scheduleQty",value = "未排产数量")
    private BigDecimal noScheduleQty;

    /**
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
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
     * 工单编码
     */
    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    @Column(name = "work_order_code")
    private String workOrderCode;

}
