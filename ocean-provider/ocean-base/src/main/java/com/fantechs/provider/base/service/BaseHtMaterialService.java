package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;

import java.util.List;

public interface BaseHtMaterialService {

    List<BaseHtMaterial> findHtList(SearchBaseMaterial searchBaseMaterial);
}
