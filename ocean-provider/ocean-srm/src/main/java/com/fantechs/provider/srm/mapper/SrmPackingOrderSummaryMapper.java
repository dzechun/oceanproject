package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummary;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmPackingOrderSummaryMapper extends MyMapper<SrmPackingOrderSummary> {
    List<SrmPackingOrderSummaryDto> findList(Map<String, Object> map);
}