package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentReEsDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentReEs;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */

public interface DaqHtEquipmentReEsService extends IService<EamHtEquipmentReEs> {
    List<EamHtEquipmentReEsDto> findHtList(Map<String, Object> map);
}
