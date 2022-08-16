package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJo;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJo;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WmsOutDespatchOrderReJoMapper extends MyMapper<WmsOutDespatchOrderReJo> {
    List<WmsOutDespatchOrderReJoDto> findList(SearchWmsOutDespatchOrderReJo searchWmsOutDespatchOrderReJo);

    Integer findCountQty(@Param("sourceOrderId")Long sourceOrderId,@Param("orderTypeId")Long orderTypeId);

    Integer findCount(@Param("sourceOrderId")Long sourceOrderId,@Param("orderTypeId")Long orderTypeId);

    int writeOutQty(@Param("orderStatus")Byte orderStatus,@Param("deliveryOrderId")Long deliveryOrderId);
}