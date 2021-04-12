package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.entity.basic.history.BaseHtProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;

import java.util.List;

public interface BaseHtProLineService {
    List<BaseHtProLine> selectHtProLines(SearchBaseProLine searchBaseProLine);
}
