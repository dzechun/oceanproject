package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/24.
 */

public interface BaseStorageMaterialService extends IService<BaseStorageMaterial> {

    List<BaseStorageMaterial> findList(Map<String, Object> map);
}
