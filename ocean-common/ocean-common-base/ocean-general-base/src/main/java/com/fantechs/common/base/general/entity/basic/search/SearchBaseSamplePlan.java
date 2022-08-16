package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchBaseSamplePlan extends BaseQuery implements Serializable {

    /**
     * 抽样方案编码
     */
    @ApiModelProperty(name="samplePlanCode",value = "抽样方案编码")
    private String samplePlanCode;

    /**
     * 抽样方案名称
     */
    @ApiModelProperty(name="samplePlanDesc",value = "抽样方案名称")
    private String samplePlanDesc;

    /**
     * 检验水平
     */
    @ApiModelProperty(name="testLevel",value = "检验水平")
    private String testLevel;

    /**
     * 检验标准类型名称
     */
    @ApiModelProperty(name="sampleStandardName",value = "检验标准类型名称")
    private Long sampleStandardName;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 抽样方案id
     */
    @ApiModelProperty(name="samplePlanId",value = "抽样方案id")
    private Long samplePlanId;


}
