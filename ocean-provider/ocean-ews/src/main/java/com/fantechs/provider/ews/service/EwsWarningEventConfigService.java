package com.fantechs.provider.ews.service;

import com.fantechs.common.base.general.dto.ews.EwsWarningEventConfigDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventConfig;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/27.
 */

public interface EwsWarningEventConfigService extends IService<EwsWarningEventConfig> {
    List<EwsWarningEventConfigDto> findList(Map<String, Object> map);

    int start(Long id);

    int stop(Long id);

    int immediately(Long id);
}
