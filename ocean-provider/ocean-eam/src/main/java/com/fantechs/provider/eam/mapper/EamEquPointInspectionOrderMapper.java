package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquPointInspectionOrderMapper extends MyMapper<EamEquPointInspectionOrder> {
    List<EamEquPointInspectionOrderDto> findList(Map<String, Object> map);
}