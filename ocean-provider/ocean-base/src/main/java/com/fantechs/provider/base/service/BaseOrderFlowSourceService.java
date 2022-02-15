package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseOrderFlowSourceImport;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlowSource;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/02/15.
 */

public interface BaseOrderFlowSourceService extends IService<BaseOrderFlowSource> {
    List<BaseOrderFlowSource> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseOrderFlowSourceImport> baseOrderFlowSourceImports);
}
