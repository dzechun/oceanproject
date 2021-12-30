package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.agv.dto.AgvCallBackDTO;
import com.fantechs.common.base.agv.dto.WarnCallbackDTO;

public interface RcsCallBackService {

    String agvCallback(AgvCallBackDTO agvCallBackDTO) throws Exception;

    int warnCallbackDTO(WarnCallbackDTO warnCallbackDTO) throws Exception;
}
