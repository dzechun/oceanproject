package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.entity.basic.history.BaseHtFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface BaseHtFactoryService {
    List<BaseHtFactory> findList(Map<String, Object> map);
}
