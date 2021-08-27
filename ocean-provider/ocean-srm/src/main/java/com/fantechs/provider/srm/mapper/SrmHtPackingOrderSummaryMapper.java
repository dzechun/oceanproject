package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummary;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtPackingOrderSummaryMapper extends MyMapper<SrmHtPackingOrderSummary> {
    List<SrmHtPackingOrderSummaryDto> findList(Map<String, Object> map);
}