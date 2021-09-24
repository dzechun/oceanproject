package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.restapi.EngReportStockOrderDto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngReportStockOrderMapper extends MyMapper<EngReportStockOrderDto> {
    List<EngReportStockOrderDto> findStockOrder(Map<String, Object> map);
}
