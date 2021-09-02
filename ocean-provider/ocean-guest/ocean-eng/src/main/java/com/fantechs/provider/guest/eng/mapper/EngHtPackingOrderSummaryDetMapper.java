package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummaryDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngHtPackingOrderSummaryDetMapper extends MyMapper<EngHtPackingOrderSummaryDet> {
    List<EngHtPackingOrderSummaryDetDto> findList(Map<String, Object> map);
}