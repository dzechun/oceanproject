package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmPlanDeliveryOrderDetMapper extends MyMapper<SrmPlanDeliveryOrderDet> {
    List<SrmPlanDeliveryOrderDetDto> findList(Map<String, Object> map);
}
