package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface EamEquipmentService extends IService<EamEquipment> {
    List<EamEquipmentDto> findList(Map<String, Object> map);
    List<EamHtEquipment> findHtList(Map<String, Object> map);
}
