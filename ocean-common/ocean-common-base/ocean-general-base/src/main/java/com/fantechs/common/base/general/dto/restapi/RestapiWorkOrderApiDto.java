package com.fantechs.common.base.general.dto.restapi;

import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import lombok.Data;

import java.io.Serializable;

@Data
public class RestapiWorkOrderApiDto implements Serializable {

    /**
     * 工单
     */
  //  @Transient
    private MesPmWorkOrder mesPmWorkOrder;

    /**
     * 工单BOOM
     */
    private MesPmWorkOrderMaterialReP mesPmWorkOrderMaterialReP;
}
