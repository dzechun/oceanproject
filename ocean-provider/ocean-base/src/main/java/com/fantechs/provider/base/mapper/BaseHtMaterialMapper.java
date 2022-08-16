package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtMaterialMapper extends MyMapper<BaseHtMaterial> {
    List<BaseHtMaterial> findHtList(Map<String, Object> map);
}