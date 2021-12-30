package com.fantechs.provider.ews.service;

import com.fantechs.common.base.general.dto.ews.EwsWarningEventExecutePushLogDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecutePushLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/28.
 */

public interface EwsWarningEventExecutePushLogService extends IService<EwsWarningEventExecutePushLog> {
    List<EwsWarningEventExecutePushLogDto> findList(Map<String, Object> map);
}
