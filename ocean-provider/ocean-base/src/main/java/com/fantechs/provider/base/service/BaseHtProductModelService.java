package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;

import java.util.List;
import java.util.Map;

public interface BaseHtProductModelService {

    List<BaseHtProductModel> selectHtProductModels(Map<String, Object> map);
}
