package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroup;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqHtEquipmentDataGroupService extends IService<EamHtEquipmentDataGroup> {
    List<EamHtEquipmentDataGroupDto> findHtList(Map<String, Object> map);
}
