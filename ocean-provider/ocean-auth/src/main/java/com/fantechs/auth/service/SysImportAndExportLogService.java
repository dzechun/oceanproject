package com.fantechs.auth.service;

import com.fantechs.common.base.dto.security.SysImportAndExportLogDto;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/07.
 */

public interface SysImportAndExportLogService extends IService<SysImportAndExportLog> {
    List<SysImportAndExportLogDto> findList(Map<String, Object> map);
}
