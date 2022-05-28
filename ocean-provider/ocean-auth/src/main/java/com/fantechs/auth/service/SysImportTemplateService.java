package com.fantechs.auth.service;

import com.fantechs.common.base.entity.security.SysImportTemplate;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

;

/**
 *
 * Created by leifengzhi on 2021/10/13.
 */

public interface SysImportTemplateService extends IService<SysImportTemplate> {
    List<SysImportTemplate> findList(Map<String, Object> map);
}
