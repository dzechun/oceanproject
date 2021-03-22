package com.fantechs.provider.log.service;


import com.fantechs.common.base.general.dto.log.SmtEmpOperationLogDto;
import com.fantechs.common.base.general.entity.log.SmtEmpOperationLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/03/12.
 */

public interface SmtEmpOperationLogService extends IService<SmtEmpOperationLog> {
    List<SmtEmpOperationLogDto> findList(Map<String, Object> map);
}
