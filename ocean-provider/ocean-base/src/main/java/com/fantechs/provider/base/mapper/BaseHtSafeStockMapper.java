package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSafeStockDto;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchOltSafeStock;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseHtSafeStockMapper extends MyMapper<BaseHtSafeStock> {
    List<BaseSafeStockDto> findHtList(SearchOltSafeStock searchOltSafeStock);
}