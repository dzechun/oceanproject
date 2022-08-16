package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtStorageMaterialMapper extends MyMapper<BaseHtStorageMaterial> {
    List<BaseHtStorageMaterial> findHtList(Map<String, Object> map);
}