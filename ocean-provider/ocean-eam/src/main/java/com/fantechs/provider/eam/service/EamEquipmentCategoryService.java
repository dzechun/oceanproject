package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentCategoryDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface EamEquipmentCategoryService extends IService<EamEquipmentCategory> {
    List<EamEquipmentCategoryDto> findList(Map<String, Object> map);
    List<EamHtEquipmentCategory> findHtList(Map<String, Object> map);
}
