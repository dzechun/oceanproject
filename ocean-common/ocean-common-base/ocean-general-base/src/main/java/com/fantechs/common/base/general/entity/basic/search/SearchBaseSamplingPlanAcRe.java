package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SearchBaseSamplingPlanAcRe extends BaseQuery implements Serializable {

    /**
     * 抽样方案ID
     */
    @ApiModelProperty(name="samplingPlanId",value = "抽样方案ID")
    private Long samplingPlanId;

    /**
     * 抽样方案AQL值表ID
     */
    @ApiModelProperty(name="samplingPlanAqlId",value = "抽样方案AQL值表ID")
    private Long samplingPlanAqlId;

    /**
     * 批量
     */
    @ApiModelProperty(name="batch",value = "批量")
    private Integer batch;

    /**
     * AQL值
     */
    @ApiModelProperty(name="aqlValue",value = "AQL值")
    private BigDecimal aqlValue;

}
