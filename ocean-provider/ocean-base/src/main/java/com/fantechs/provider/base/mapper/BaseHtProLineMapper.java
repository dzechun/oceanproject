package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.history.BaseHtProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtProLineMapper extends MyMapper<BaseHtProLine> {

    List<BaseHtProLine> selectHtProLines(Map<String, Object> map);
}