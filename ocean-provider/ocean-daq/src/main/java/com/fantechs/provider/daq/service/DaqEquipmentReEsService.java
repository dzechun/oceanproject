package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentReEsDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentReEs;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */

public interface DaqEquipmentReEsService extends IService<DaqEquipmentReEs> {
    List<DaqEquipmentReEsDto> findList(Map<String, Object> map);
}
