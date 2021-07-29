package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamDataCollectDto;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/19.
 */

public interface EamDataCollectService extends IService<EamDataCollect> {
    List<EamDataCollectDto> findList(Map<String, Object> map);

    List<EamDataCollectDto> findByGroup(Long equipmentId);
}
