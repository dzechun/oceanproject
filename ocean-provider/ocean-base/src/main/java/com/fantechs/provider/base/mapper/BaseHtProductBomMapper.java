package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBom;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtProductBomMapper extends MyMapper<BaseHtProductBom> {
    List<BaseHtProductBom> findList(Map<String, Object> map);
}