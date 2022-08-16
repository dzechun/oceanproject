package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsOutDespatchOrderReJoReDetMapper extends MyMapper<WmsOutDespatchOrderReJoReDet> {
    List<WmsOutDespatchOrderReJoReDetDto> findList(SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet);
}