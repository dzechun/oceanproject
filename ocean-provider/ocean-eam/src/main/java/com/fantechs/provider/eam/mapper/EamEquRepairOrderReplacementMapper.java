package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquRepairOrderReplacementDto;
import com.fantechs.common.base.general.entity.eam.EamEquRepairOrderReplacement;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquRepairOrderReplacementMapper extends MyMapper<EamEquRepairOrderReplacement> {
    List<EamEquRepairOrderReplacementDto> findList(Map<String,Object> map);
}