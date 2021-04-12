package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseStorageMaterialMapper extends MyMapper<BaseStorageMaterial> {
    List<BaseStorageMaterial> findList(SearchBaseStorageMaterial searchBaseStorageMaterial);
}