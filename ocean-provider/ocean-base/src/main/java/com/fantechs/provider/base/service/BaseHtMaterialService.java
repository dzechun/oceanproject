package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;

import java.util.List;
import java.util.Map;

public interface BaseHtMaterialService {

    List<BaseHtMaterial> findHtList(Map<String, Object> map);
}
