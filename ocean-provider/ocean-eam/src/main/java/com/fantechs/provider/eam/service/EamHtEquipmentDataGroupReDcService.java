package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroupReDc;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface EamHtEquipmentDataGroupReDcService extends IService<EamHtEquipmentDataGroupReDc> {
    List<EamHtEquipmentDataGroupReDcDto> findHtList(Map<String, Object> map);
}
