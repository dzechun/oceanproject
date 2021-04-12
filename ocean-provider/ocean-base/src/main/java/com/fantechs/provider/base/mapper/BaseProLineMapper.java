package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseProLineMapper extends MyMapper<BaseProLine> {

    List<BaseProLine> findList(SearchBaseProLine searchBaseProLine);
}