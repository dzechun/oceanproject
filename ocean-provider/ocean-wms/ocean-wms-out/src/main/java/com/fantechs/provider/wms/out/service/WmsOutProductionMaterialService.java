package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutProductionMaterialDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.support.IService;


import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/18.
 */

public interface WmsOutProductionMaterialService extends IService<WmsOutProductionMaterial> {
    List<WmsOutProductionMaterialDto> findList(Map<String, Object> map);

    List<WmsOutProductionMaterial> findHtList(Map<String, Object> dynamicConditionByEntity);

    int updateByWorkOrderId(WmsOutProductionMaterial wmsOutProductionMaterial);
}
