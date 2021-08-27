package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummaryDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtPackingOrderSummaryDetMapper extends MyMapper<SrmHtPackingOrderSummaryDet> {
    List<SrmHtPackingOrderSummaryDetDto> findList(Map<String, Object> map);
}