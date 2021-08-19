package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentStationDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */

public interface DaqHtEquipmentStationService extends IService<EamHtEquipmentStation> {
    List<EamHtEquipmentStationDto> findHtList(Map<String, Object> map);
}
