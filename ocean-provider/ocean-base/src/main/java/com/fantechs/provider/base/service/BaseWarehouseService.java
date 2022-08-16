package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface BaseWarehouseService extends IService<BaseWarehouse>{
    List<BaseWarehouse> findList(Map<String, Object> map);

    //更新编码批量更新仓库
    int batchUpdateByCode(List<BaseWarehouse> baseWarehouses);

    int insertList(List<BaseWarehouse> baseWarehouses);

    Map<String, Object> importExcel(List<BaseWarehouseImport> baseWarehouseImports);

}
