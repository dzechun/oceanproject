package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmPlanDeliveryOrderMapper extends MyMapper<SrmPlanDeliveryOrder> {
    List<SrmPlanDeliveryOrderDto> findList(Map<String, Object> map);
}
