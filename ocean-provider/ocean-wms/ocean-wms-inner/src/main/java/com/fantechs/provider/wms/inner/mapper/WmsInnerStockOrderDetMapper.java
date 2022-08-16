package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerStockOrderDetMapper extends MyMapper<WmsInnerStockOrderDet> {
    List<WmsInnerStockOrderDetDto> findList(Map<String,Object> map);
}