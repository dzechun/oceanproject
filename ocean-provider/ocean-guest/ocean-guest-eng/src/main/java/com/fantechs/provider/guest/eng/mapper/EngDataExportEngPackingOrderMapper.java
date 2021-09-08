package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.restapi.EngDataExportEngPackingOrderDto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngDataExportEngPackingOrderMapper extends MyMapper<EngDataExportEngPackingOrderDto> {
    List<EngDataExportEngPackingOrderDto> findExportData(Map<String, Object> map);
}
