package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShop;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface BaseHtWorkShopService {
    List<BaseHtWorkShop> findList(Map<String, Object> map);
}
