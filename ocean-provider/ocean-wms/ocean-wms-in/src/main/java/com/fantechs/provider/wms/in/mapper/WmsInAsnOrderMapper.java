package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsInAsnOrderMapper extends MyMapper<WmsInAsnOrder> {
    List<WmsInAsnOrderDto> findList(SearchWmsInAsnOrder searchWmsInAsnOrder);
}