package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigRequisitionWorkOrderDto;
import com.fantechs.common.base.general.dto.eam.EamJigReturnDto;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigReturn;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/30.
 */

public interface EamJigReturnService extends IService<EamJigReturn> {
    List<EamJigReturnDto> findList(Map<String, Object> map);

    EamJigRequisitionWorkOrderDto findWorkOrder(String workOrderCode);

    EamJigBarcode checkJigBarcode(String jigBarcode, Long jigId, Long workOrderId);

    int checkStorageCode(String storageCode, Long jigId);
}
