package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BaseHtLabelCategoryService extends IService<BaseHtLabelCategory> {
    List<BaseHtLabelCategory> findList(Map<String, Object> map);
}
