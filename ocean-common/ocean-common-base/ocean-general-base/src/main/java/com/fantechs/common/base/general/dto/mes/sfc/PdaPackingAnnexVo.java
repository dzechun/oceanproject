package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdaPackingAnnexVo implements Serializable {

    @ApiModelProperty(name = "barCode", value = "附件码", required = true)
    private String barCode;
    @ApiModelProperty(name = "stationId", value = "工位ID", required = true)
    private Long stationId;
}
