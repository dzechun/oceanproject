package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatePartsImport;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */

public interface BasePlatePartsService extends IService<BasePlateParts> {
    List<BasePlatePartsDto> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BasePlatePartsImport> basePlatePartsImports);
}
