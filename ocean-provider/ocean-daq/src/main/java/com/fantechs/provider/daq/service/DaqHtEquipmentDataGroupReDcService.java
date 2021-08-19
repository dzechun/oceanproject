package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroupReDc;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqHtEquipmentDataGroupReDcService extends IService<DaqHtEquipmentDataGroupReDc> {
    List<DaqHtEquipmentDataGroupReDcDto> findHtList(Map<String, Object> map);
}
