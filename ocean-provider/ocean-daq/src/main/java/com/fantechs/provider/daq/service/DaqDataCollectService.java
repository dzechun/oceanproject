package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqDataCollectDto;
import com.fantechs.common.base.general.dto.daq.DaqDataCollectModel;
import com.fantechs.common.base.general.entity.daq.DaqDataCollect;
import com.fantechs.common.base.general.entity.daq.search.SearchDaqEquipmentDataGroup;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/19.
 */

public interface DaqDataCollectService extends IService<DaqDataCollect> {
    List<DaqDataCollectDto> findList(Map<String, Object> map);

    List<DaqDataCollectDto> findByEquipmentId(Long equipmentId);

    DaqDataCollectModel findByGroup(SearchDaqEquipmentDataGroup searchEamEquipmentDataGroup);

}
