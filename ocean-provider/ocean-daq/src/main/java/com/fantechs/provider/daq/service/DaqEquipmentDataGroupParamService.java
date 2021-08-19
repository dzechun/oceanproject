package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupParam;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqEquipmentDataGroupParamService extends IService<EamEquipmentDataGroupParam> {
    List<EamEquipmentDataGroupParamDto> findList(Map<String, Object> map);
}
