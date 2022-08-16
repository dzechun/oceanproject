package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.entity.basic.history.BaseHtProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;

import java.util.List;
import java.util.Map;

public interface BaseHtProLineService {
    List<BaseHtProLine> selectHtProLines(Map<String, Object> map);
}
