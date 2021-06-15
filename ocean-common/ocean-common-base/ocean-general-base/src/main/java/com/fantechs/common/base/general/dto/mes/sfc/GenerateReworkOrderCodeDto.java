package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateReworkOrderCodeDto implements Serializable {

    @ApiModelProperty(name = "reworkOrderCode", value = "返工单号")
    private String reworkOrderCode;
}
