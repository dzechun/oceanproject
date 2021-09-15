package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.restapi.EngReportInnerJobOrderDto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngReportInnerJobOrderMapper extends MyMapper<EngReportInnerJobOrderDto> {
    List<EngReportInnerJobOrderDto> findInnerJobOrder(Map<String, Object> map);
}
