package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/28.
 */

public interface EamHtEquipmentMaterialService extends IService<EamHtEquipmentMaterial> {
    List<EamHtEquipmentMaterial> findHtList(Map<String, Object> map);
}
