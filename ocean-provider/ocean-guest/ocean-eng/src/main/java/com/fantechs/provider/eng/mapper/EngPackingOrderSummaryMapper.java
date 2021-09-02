package com.fantechs.provider.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngPackingOrderSummaryMapper extends MyMapper<EngPackingOrderSummary> {
    List<EngPackingOrderSummaryDto> findList(Map<String, Object> map);
}