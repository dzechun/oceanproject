package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquPointInspectionOrderDetMapper extends MyMapper<EamEquPointInspectionOrderDet> {
    List<EamEquPointInspectionOrderDetDto> findList(Map<String, Object> map);
}