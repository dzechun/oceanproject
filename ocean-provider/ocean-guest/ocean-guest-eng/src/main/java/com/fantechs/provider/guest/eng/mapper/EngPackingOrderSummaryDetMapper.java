package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngPackingOrderSummaryDetMapper extends MyMapper<EngPackingOrderSummaryDet> {
    List<EngPackingOrderSummaryDetDto> findList(Map<String, Object> map);

    Long findExistCount(@Param("packingOrderId")Long packingOrderId);
}