package com.fantechs.provider.ews.service;

import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigReWuiDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfigReWui;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/27.
 */

public interface EwsWarningPushConfigReWuiService extends IService<EwsWarningPushConfigReWui> {
    List<EwsWarningPushConfigReWuiDto> findList(Map<String, Object> map);
}
