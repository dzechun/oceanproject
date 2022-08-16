package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/31.
 */

public interface BaseHtMaterialCategoryService extends IService<BaseHtMaterialCategory> {

    List<BaseHtMaterialCategory> findHtList(Map<String, Object> map);
}
