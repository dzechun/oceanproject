package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchBaseSamplingPlan extends BaseQuery implements Serializable {

    /**
     * 抽样方案编码
     */
    @ApiModelProperty(name="samplingPlanCode",value = "抽样方案编码")
    private String samplingPlanCode;

    /**
     * 抽样方案名称
     */
    @ApiModelProperty(name="samplingPlanDesc",value = "抽样方案名称")
    private String samplingPlanDesc;

    /**
     * 检验水平
     */
    @ApiModelProperty(name="testLevel",value = "检验水平")
    private String testLevel;

    /**
     * 检验标准类型
     */
    @ApiModelProperty(name="inspectionTypeId",value = "检验标准类型")
    private Long inspectionTypeId;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;


}
