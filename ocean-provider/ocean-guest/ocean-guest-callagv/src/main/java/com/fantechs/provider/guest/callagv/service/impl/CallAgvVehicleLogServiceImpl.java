package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.guest.callagv.mapper.CallAgvVehicleLogMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CallAgvVehicleLogServiceImpl extends BaseService<CallAgvVehicleLog> implements CallAgvVehicleLogService {

    @Resource
    private CallAgvVehicleLogMapper callAgvVehicleLogMapper;

    @Override
    public List<CallAgvVehicleLogDto> findList(Map<String, Object> map) {
        return callAgvVehicleLogMapper.findList(map);
    }
}
