package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.agv.dto.AgvCallBackDTO;

public interface RcsCallBackService {

    String agvCallback(AgvCallBackDTO agvCallBackDTO) throws Exception;
}
