package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.history.BaseHtFactory;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtFactoryMapper extends MyMapper<BaseHtFactory> {
    List<BaseHtFactory> findList(Map<String, Object> map);
}