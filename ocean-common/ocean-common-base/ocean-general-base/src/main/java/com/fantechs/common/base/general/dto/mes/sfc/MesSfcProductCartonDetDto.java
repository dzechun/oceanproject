package com.fantechs.common.base.general.dto.mes.sfc;


import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCartonDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class MesSfcProductCartonDetDto extends MesSfcProductCartonDet implements Serializable {

    /**
     * 条码
     */
    @Transient
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;
}
