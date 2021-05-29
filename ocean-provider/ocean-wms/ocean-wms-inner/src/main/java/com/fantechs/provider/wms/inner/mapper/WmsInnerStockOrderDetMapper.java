package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInventoryVerificationDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInventoryVerificationDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerStockOrderDetMapper extends MyMapper<WmsInventoryVerificationDet> {
    List<WmsInventoryVerificationDetDto> findList(Map<String,Object> map);
}