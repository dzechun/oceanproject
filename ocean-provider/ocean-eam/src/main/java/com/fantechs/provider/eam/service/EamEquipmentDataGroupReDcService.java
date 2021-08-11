package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupReDc;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface EamEquipmentDataGroupReDcService extends IService<EamEquipmentDataGroupReDc> {
    List<EamEquipmentDataGroupReDcDto> findList(Map<String, Object> map);

    int batchAdd(List<EamEquipmentDataGroupReDc> eamEquipmentDataGroupReDcs);
}
