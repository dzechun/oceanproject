package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdaCartonAnnexDto extends PdaCartonDto implements Serializable {

    @ApiModelProperty(name = "barAnnexCode", value = "附件码", required = true)
    private String barAnnexCode;

    @ApiModelProperty(name = "proLineId", value = "产线ID", required = true)
    private Long proLineId;
}
