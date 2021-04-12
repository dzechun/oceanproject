package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/24.
 */

public interface BaseHtStorageMaterialService extends IService<BaseHtStorageMaterial> {

    List<BaseHtStorageMaterial> findHtList(SearchBaseStorageMaterial searchBaseStorageMaterial);
}
