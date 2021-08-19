package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroup;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */

public interface DaqEquipmentDataGroupService extends IService<DaqEquipmentDataGroup> {
    List<DaqEquipmentDataGroupDto> findList(Map<String, Object> map);
    int save(DaqEquipmentDataGroupDto daqEquipmentDataGroupDto);
    int update(DaqEquipmentDataGroupDto daqEquipmentDataGroupDto);
    int batchDelete(String ids);
}
