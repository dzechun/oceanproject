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
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesPmDailyPlan extends BaseQuery implements Serializable {

    @ApiModelProperty(name="dailyPlanId",value = "日计划ID")
    private Long dailyPlanId;

    @ApiModelProperty(name="proLineId",value = "产线ID")
    private Long proLineId;

    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    private String coreSourceSysOrderTypeCode;

    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    private String sourceSysOrderTypeCode;

    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    private String sysOrderTypeCode;

    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    private Byte sourceBigType;

    @ApiModelProperty(name="dailyPlanCode",value = "计划单号")
    private String dailyPlanCode;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @ApiModelProperty(name="workOrderType",value = "工单类型(0、量产 1、试产 2、返工 3、维修)")
    private Byte workOrderType;

    /**
     * 计划开始时间
     */
    @ApiModelProperty(name="planStartTime",value = "计划开始时间")
    private String planStartTime;

    /**
     * 是否插单(0-否 1-是)
     */
    @ApiModelProperty(name="ifOrderInserting",value = "是否插单(0-否 1-是)")
    private Byte ifOrderInserting;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;


}
