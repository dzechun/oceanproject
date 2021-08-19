package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentReEsDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentReEs;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */

public interface DaqHtEquipmentReEsService extends IService<DaqHtEquipmentReEs> {
    List<DaqHtEquipmentReEsDto> findHtList(Map<String, Object> map);
}
