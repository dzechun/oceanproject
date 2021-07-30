package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigRequisitionDto;
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

    EamJigRequisitionDto findWorkOrder(String workOrderCode);

    EamJigRequisitionDto checkJigBarcode(String jigBarcode,Long jigId);
}
