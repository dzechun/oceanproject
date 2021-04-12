package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.OltSafeStockDto;
import com.fantechs.common.base.general.entity.basic.history.OltHtSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchOltSafeStock;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OltHtSafeStockMapper extends MyMapper<OltHtSafeStock> {
    List<OltSafeStockDto> findHtList(SearchOltSafeStock searchOltSafeStock);
}