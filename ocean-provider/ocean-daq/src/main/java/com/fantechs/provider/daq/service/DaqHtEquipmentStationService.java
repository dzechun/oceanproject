package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentStationDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentStation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */

public interface DaqHtEquipmentStationService extends IService<DaqHtEquipmentStation> {
    List<DaqHtEquipmentStationDto> findHtList(Map<String, Object> map);
}
