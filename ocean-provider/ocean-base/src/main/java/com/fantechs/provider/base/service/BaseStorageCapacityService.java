package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseStorageCapacityImport;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/18.
 */

public interface BaseStorageCapacityService extends IService<BaseStorageCapacity> {
    List<BaseStorageCapacity> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseStorageCapacityImport> baseStorageCapacityImports);
}
