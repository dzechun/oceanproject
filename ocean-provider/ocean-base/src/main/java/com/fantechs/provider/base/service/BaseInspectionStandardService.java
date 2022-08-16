package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionStandardImport;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */

public interface BaseInspectionStandardService extends IService<BaseInspectionStandard> {
    List<BaseInspectionStandard> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseInspectionStandardImport> baseInspectionStandardImports);
}
