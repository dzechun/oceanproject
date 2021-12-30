package com.fantechs.provider.ews.service;

import com.fantechs.common.base.general.dto.ews.EwsHtWarningPushConfigDto;
import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfig;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/27.
 */

public interface EwsWarningPushConfigService extends IService<EwsWarningPushConfig> {
    List<EwsWarningPushConfigDto> findList(Map<String, Object> map);

    List<EwsHtWarningPushConfigDto> findHtList(Map<String,Object> map);
}
