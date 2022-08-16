package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SearchBaseSamplePlanAcRe extends BaseQuery implements Serializable {

    /**
     * 抽样方案ID
     */
    @ApiModelProperty(name="samplePlanId",value = "抽样方案ID")
    private Long samplePlanId;

    /**
     * 抽样方案AQL值表ID
     */
    @ApiModelProperty(name="samplePlanAqlId",value = "抽样方案AQL值表ID")
    private Long samplePlanAqlId;

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
