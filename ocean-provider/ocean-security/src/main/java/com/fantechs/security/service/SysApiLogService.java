package com.fantechs.security.service;

import com.fantechs.common.base.dto.security.SysApiLogDto;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/07/09.
 */

public interface SysApiLogService extends IService<SysApiLog> {
    List<SysApiLogDto> findList(Map<String, Object> map);

    /**
     * 批量插入接口日志
     * @param logList
     * @return
     */
    int batchAdd(List<SysApiLog> logList);
}
