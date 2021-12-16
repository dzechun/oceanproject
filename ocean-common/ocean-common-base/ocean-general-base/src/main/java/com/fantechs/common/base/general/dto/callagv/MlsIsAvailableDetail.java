package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MlsIsAvailableDetail {

    @ApiModelProperty(name = "materialCode", value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name = "number", value = "物料数量")
    private Long number;
}
