package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseProcessMapper extends MyMapper<BaseProcess> {
    List<BaseProcess> findList(SearchBaseProcess searchBaseProcess);
}