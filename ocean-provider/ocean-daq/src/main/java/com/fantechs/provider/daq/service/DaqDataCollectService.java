package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.eam.EamDataCollectDto;
import com.fantechs.common.base.general.dto.eam.EamDataCollectModel;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentDataGroup;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/19.
 */

public interface DaqDataCollectService extends IService<EamDataCollect> {
    List<EamDataCollectDto> findList(Map<String, Object> map);

    List<EamDataCollectDto> findByEquipmentId(Long equipmentId);

    EamDataCollectModel findByGroup(SearchEamEquipmentDataGroup searchEamEquipmentDataGroup);

}
