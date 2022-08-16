package com.fantechs.provider.base.mapper;


import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShop;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtWorkShopMapper extends MyMapper<BaseHtWorkShop> {
    List<BaseHtWorkShop> findList(Map<String, Object> map);
}