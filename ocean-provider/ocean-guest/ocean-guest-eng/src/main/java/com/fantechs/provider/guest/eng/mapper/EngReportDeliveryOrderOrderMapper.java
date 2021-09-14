package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.restapi.EngReportDeliveryOrderOrderDto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngReportDeliveryOrderOrderMapper extends MyMapper<EngReportDeliveryOrderOrderDto> {
    List<EngReportDeliveryOrderOrderDto> findDeliveryOrderOrder(Map<String, Object> map);
}
