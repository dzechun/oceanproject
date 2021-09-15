package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.restapi.EngReportDeliveryOrderDto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngReportDeliveryOrderOrderMapper extends MyMapper<EngReportDeliveryOrderDto> {
    List<EngReportDeliveryOrderDto> findDeliveryOrderOrder(Map<String, Object> map);
}
