package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInAndOutRuleDet extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -3896088051789821762L;
    /**
     * 出入库规则ID
     */
    @ApiModelProperty(name="inAndOutRuleId" ,value="出入库规则ID")
    private Long inAndOutRuleId;


}
