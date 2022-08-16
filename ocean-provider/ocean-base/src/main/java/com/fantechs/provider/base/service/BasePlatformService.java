package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BasePlatformImport;
import com.fantechs.common.base.general.entity.basic.BasePlatform;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/14.
 */

public interface BasePlatformService extends IService<BasePlatform> {
    List<BasePlatform> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BasePlatformImport> basePlatformImports);
}
