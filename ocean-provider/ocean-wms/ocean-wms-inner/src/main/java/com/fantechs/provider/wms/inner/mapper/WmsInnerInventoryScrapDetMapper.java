package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrapDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerInventoryScrapDetMapper extends MyMapper<WmsInnerInventoryScrapDet> {
    List<WmsInnerInventoryScrapDetDto> findList(Map<String, Object> map);
}