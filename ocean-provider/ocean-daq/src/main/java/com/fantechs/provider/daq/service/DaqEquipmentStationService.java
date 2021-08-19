package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentStationDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentStation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */

public interface DaqEquipmentStationService extends IService<DaqEquipmentStation> {

    List<DaqEquipmentStationDto> findList(Map<String, Object> map);
    int save(DaqEquipmentStationDto daqEquipmentStationDto);
    int update(DaqEquipmentStationDto daqEquipmentStationDto);
    int batchDelete(String ids);
}
