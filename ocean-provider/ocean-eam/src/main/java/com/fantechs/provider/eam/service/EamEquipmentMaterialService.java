package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/28.
 */

public interface EamEquipmentMaterialService extends IService<EamEquipmentMaterial> {
    List<EamEquipmentMaterialDto> findList(Map<String, Object> map);
}
