package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseBarcodeRuleSpec extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 4466742559404003403L;

    /**
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    private Long barcodeRuleId;
}
