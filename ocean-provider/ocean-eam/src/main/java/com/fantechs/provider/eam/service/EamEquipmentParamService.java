package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParam;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface EamEquipmentParamService extends IService<EamEquipmentParam> {
    List<EamEquipmentParamDto> findList(Map<String, Object> map);
}
