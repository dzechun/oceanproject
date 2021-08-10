package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStationDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */

public interface EamEquipmentStationService extends IService<EamEquipmentStation> {

    List<EamEquipmentStationDto> findList(Map<String, Object> map);
    int save(EamEquipmentStationDto eamEquipmentStationDto);
    int update(EamEquipmentStationDto eamEquipmentStationDto);
    int batchDelete(String ids);
}
