package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktaking;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerHtStocktakingMapper extends MyMapper<WmsInnerHtStocktaking> {

    List<WmsInnerHtStocktaking> findHtList(Map<String, Object> map);
}