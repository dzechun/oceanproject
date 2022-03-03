package com.fantechs.provider.ews.service;

import com.fantechs.common.base.general.dto.ews.EwsWarningEventExecuteLogDto;
import com.fantechs.common.base.general.dto.ews.LogUreportDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecuteLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/28.
 */

public interface EwsWarningEventExecuteLogService extends IService<EwsWarningEventExecuteLog> {
    List<EwsWarningEventExecuteLogDto> findList(Map<String, Object> map);

    int push();

    List<LogUreportDto> findLogUreport(Map<String ,Object> map);

    int affirm(Long warningEventExecutePushLogId);
}
