package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseLabelVariableDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseLabelVariableImport;
import com.fantechs.common.base.general.entity.basic.BaseLabelVariable;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelVariable;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */

public interface BaseLabelVariableService extends IService<BaseLabelVariable> {
    List<BaseLabelVariableDto> findList(Map<String, Object> map);

    List<BaseHtLabelVariable> findHtList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseLabelVariableImport> baseLabelVariableImports);
}
