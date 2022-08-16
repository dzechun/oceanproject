package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessCategory;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtProcessCategoryMapper extends MyMapper<BaseHtProcessCategory> {

    List<BaseHtProcessCategory> findHtList(Map<String,Object> map);
}