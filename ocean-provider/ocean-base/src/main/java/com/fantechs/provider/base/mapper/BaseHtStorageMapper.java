package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtStorageMapper extends MyMapper<BaseHtStorage> {
    List<BaseHtStorage> findHtList(SearchBaseStorage searchBaseStorage);
}