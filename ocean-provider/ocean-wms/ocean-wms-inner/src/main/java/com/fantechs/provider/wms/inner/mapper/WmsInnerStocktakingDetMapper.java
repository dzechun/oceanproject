package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktakingDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerStocktakingDetMapper extends MyMapper<WmsInnerStocktakingDet> {

    List<WmsInnerStocktakingDetDto> findList(Map<String, Object> map);
}