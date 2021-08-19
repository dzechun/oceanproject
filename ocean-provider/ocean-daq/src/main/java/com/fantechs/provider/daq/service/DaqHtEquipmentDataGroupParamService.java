package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroupParam;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqHtEquipmentDataGroupParamService extends IService<DaqHtEquipmentDataGroupParam> {
    List<DaqHtEquipmentDataGroupParamDto> findHtList(Map<String, Object> map);
}
