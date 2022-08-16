package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseProcessMapper extends MyMapper<BaseProcess> {
    List<BaseProcess> findList(Map<String, Object> map);
}