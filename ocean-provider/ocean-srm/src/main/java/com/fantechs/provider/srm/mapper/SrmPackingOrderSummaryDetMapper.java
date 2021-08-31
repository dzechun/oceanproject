package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummaryDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmPackingOrderSummaryDetMapper extends MyMapper<SrmPackingOrderSummaryDet> {
    List<SrmPackingOrderSummaryDetDto> findList(Map<String, Object> map);
}