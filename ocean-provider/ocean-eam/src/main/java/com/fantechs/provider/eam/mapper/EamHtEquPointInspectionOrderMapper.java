package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquPointInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquPointInspectionOrderMapper extends MyMapper<EamHtEquPointInspectionOrder> {
    List<EamEquPointInspectionOrderDto> findList(Map<String, Object> map);
}