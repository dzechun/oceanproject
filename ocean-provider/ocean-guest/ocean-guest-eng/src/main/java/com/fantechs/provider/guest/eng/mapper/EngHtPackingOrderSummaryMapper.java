package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummary;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngHtPackingOrderSummaryMapper extends MyMapper<EngHtPackingOrderSummary> {
    List<EngHtPackingOrderSummaryDto> findList(Map<String, Object> map);
}