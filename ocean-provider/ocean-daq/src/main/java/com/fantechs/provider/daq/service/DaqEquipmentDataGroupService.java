package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroup;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqEquipmentDataGroupService extends IService<EamEquipmentDataGroup> {
    List<EamEquipmentDataGroupDto> findList(Map<String, Object> map);
    int save(EamEquipmentDataGroupDto eamEquipmentDataGroupDto);
    int update(EamEquipmentDataGroupDto eamEquipmentDataGroupDto);
    int batchDelete(String ids);
}
