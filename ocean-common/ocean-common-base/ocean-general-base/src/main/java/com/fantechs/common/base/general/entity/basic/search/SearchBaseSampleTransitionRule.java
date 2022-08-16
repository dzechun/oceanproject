package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseSampleTransitionRule extends BaseQuery implements Serializable {

    /**
     * 抽样转移规则编码
     */
    @ApiModelProperty(name="sampleTransitionRuleCode",value = "抽样转移规则编码")
    private String sampleTransitionRuleCode;

    /**
     * 抽样转移规则描述
     */
    @ApiModelProperty(name="sampleTransitionRuleDesc",value = "抽样转移规则描述")
    private String sampleTransitionRuleDesc;

    private static final long serialVersionUID = 1L;
}
