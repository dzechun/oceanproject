package com.fantechs.common.base.general.dto.mes.sfc;


import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCartonDet;
import lombok.Data;

import java.io.Serializable;

@Data
public class MesSfcProductCartonDetDto extends MesSfcProductCartonDet implements Serializable {
    private  Byte  closeStatus;
}
