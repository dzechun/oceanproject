package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseAreaImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkingAreaImport;
import com.fantechs.common.base.general.entity.basic.BaseWorkingArea;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */

public interface BaseWorkingAreaService extends IService<BaseWorkingArea> {
    List<BaseWorkingAreaDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseWorkingAreaImport> baseWorkingAreaImports);
}
