package com.fantechs.provider.wms.inner.mapper;


import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerStocktakingMapper extends MyMapper<WmsInnerStocktaking> {

    List<WmsInnerStocktakingDto> findList(Map<String, Object> map);
}