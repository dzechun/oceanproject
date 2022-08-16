package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SearchBaseSamplePlanAql extends BaseQuery implements Serializable {

    /**
     * 检验严格度(1-放宽 2-正常 3-加严)
     */
    @ApiModelProperty(name="inspectionRigorLevel",value = "检验严格度(1-放宽 2-正常 3-加严)")
    private Byte inspectionRigorLevel;

    /**
     * AQL值
     */
    @ApiModelProperty(name="aqlValue",value = "AQL值")
    private BigDecimal aqlValue;

    /**
     * 抽样方案ID
     */
    @ApiModelProperty(name="samplePlanId",value = "抽样方案ID")
    private Long samplePlanId;

}
