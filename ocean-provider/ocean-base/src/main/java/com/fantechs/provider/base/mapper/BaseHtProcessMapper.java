package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtProcessMapper extends MyMapper<BaseHtProcess> {
    List<BaseHtProcess> findHtList(SearchBaseProcess searchBaseProcess);
}