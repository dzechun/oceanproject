package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseMaterialSupplierDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseMaterialSupplierImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialSupplier;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/11/03.
 */

public interface BaseMaterialSupplierService extends IService<BaseMaterialSupplier> {

    List<BaseMaterialSupplierDto> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseMaterialSupplierImport> baseMaterialSupplierImports);
    List<BaseHtMaterialSupplier> findHtList(Map<String, Object> map);
}
