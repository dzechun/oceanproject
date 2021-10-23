package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.general.dto.callagv.CallAgvProductionInLogDetDto;
import com.fantechs.common.base.general.dto.callagv.CallAgvProductionInLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvProductionInLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface CallAgvProductionInLogService extends IService<CallAgvProductionInLog> {
    List<CallAgvProductionInLogDto> findList(Map<String, Object> map);

    List<CallAgvProductionInLogDetDto> findDetList(Map<String, Object> map);

    Map<String, Object> export(Map<String, Object> map);
}
