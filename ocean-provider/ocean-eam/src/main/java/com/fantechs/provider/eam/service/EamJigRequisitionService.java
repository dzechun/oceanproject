package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionWorkOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/30.
 */

public interface EamJigRequisitionService extends IService<EamJigRequisition> {
    List<EamJigRequisitionDto> findList(Map<String, Object> map);

    EamJigRequisitionWorkOrderDto findWorkOrder(String workOrderCode);

    EamJigBarcode checkJigBarcode(String jigBarcode,Long jigId);

    List<EamJigMaterialDto> getRecordQty(String newWorkOrderCode, String oldWorkOrderCode);

    int conversion(List<EamJigRequisition> list);
}
