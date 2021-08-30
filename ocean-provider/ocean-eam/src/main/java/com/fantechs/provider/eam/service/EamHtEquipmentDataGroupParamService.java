package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroupParam;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface EamHtEquipmentDataGroupParamService extends IService<EamHtEquipmentDataGroupParam> {
    List<EamHtEquipmentDataGroupParamDto> findHtList(Map<String, Object> map);
}