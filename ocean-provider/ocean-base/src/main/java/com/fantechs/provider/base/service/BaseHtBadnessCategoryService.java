package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/02.
 */

public interface BaseHtBadnessCategoryService extends IService<BaseHtBadnessCategory> {
    List<BaseHtBadnessCategory> findList(Map<String, Object> map);
}
