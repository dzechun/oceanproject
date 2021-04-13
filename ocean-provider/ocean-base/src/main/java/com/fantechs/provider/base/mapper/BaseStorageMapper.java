package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;


public interface BaseStorageMapper extends MyMapper<BaseStorage> {
    List<BaseStorage> findList(SearchBaseStorage searchBaseStorage);
    //批量更新
    int batchUpdate(List<BaseStorage> baseStorages);
}