package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutDespatchOrderMapper extends MyMapper<WmsOutDespatchOrder> {
    List<WmsOutDespatchOrderDto> findList(SearchWmsOutDespatchOrder searchWmsOutDespatchOrder);

    Long findOmWarehouseId(@Param("sourceId")Long sourceId);

    Long findStorageId(Map<String,Object> map);
}