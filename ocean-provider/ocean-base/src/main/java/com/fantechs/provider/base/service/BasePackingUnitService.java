package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BasePackingUnitDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePackingUnitImport;
import com.fantechs.common.base.general.entity.basic.BasePackingUnit;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/03.
 */

public interface BasePackingUnitService extends IService<BasePackingUnit> {

    List<BasePackingUnitDto> findList(Map<String,Object> map);
    Map<String, Object> importExcel(List<BasePackingUnitImport> basePackingUnitImports);
}
