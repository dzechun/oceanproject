package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroup;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqHtEquipmentDataGroupService extends IService<DaqHtEquipmentDataGroup> {
    List<DaqHtEquipmentDataGroupDto> findHtList(Map<String, Object> map);
}
