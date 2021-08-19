package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroupParam;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqEquipmentDataGroupParamService extends IService<DaqEquipmentDataGroupParam> {
    List<DaqEquipmentDataGroupParamDto> findList(Map<String, Object> map);
}
