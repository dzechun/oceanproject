package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface CallAgvVehicleLogService extends IService<CallAgvVehicleLog> {
    List<CallAgvVehicleLogDto> findList(Map<String, Object> map);
}
