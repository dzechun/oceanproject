package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerJobOrderDetMapper extends MyMapper<WmsInnerJobOrderDet> {
    List<WmsInnerJobOrderDetDto> findList(SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet);
}