package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/10/15.
 */

public interface BaseHtProcessCategoryService extends IService<BaseHtProcessCategory> {

    List<BaseHtProcessCategory> findHtList(Map<String,Object> map);
}
