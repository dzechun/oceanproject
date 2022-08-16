package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInitStockDetMapper extends MyMapper<WmsInnerInitStockDet> {
    List<WmsInnerInitStockDetDto> findList(Map<String,Object> map);
}