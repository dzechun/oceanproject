package com.fantechs.common.base.general.dto.mes.sfc;


import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPalletDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MesSfcProductPalletDetDto extends MesSfcProductPalletDet implements Serializable {

    @ApiModelProperty(name="cartonCode",value = "包箱号")
    private String cartonCode;
}
