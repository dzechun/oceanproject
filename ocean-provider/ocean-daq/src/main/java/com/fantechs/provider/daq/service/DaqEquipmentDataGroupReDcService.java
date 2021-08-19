package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroupReDc;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqEquipmentDataGroupReDcService extends IService<DaqEquipmentDataGroupReDc> {
    List<DaqEquipmentDataGroupReDcDto> findList(Map<String, Object> map);

    int batchAdd(List<DaqEquipmentDataGroupReDc> daqEquipmentDataGroupReDcs);
}
