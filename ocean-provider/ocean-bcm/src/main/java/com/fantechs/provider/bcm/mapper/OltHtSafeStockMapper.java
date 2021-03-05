package com.fantechs.provider.bcm.mapper;

import com.fantechs.common.base.general.dto.bcm.OltSafeStockDto;
import com.fantechs.common.base.general.entity.bcm.history.OltHtSafeStock;
import com.fantechs.common.base.general.entity.bcm.search.SearchOltSafeStock;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OltHtSafeStockMapper extends MyMapper<OltHtSafeStock> {
    List<OltSafeStockDto> findHtList(SearchOltSafeStock searchOltSafeStock);
}