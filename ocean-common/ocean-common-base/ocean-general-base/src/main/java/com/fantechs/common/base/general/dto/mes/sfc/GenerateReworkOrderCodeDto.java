package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GenerateReworkOrderCodeDto implements Serializable {

    @ApiModelProperty(name = "reworkOrderCode", value = "返工单号")
    private String reworkOrderCode;

    @ApiModelProperty(name = "keyPartRelevanceDtos", value = "部件信息")
    private List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos;
}
