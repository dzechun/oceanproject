package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktakingDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerHtStocktakingDetMapper extends MyMapper<WmsInnerHtStocktakingDet> {

    List<WmsInnerHtStocktakingDet> findHtList(Map<String, Object> map);
}