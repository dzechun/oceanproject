package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigRepairOrderReplacementDto;
import com.fantechs.common.base.general.entity.eam.EamJigRepairOrderReplacement;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigRepairOrderReplacementMapper extends MyMapper<EamJigRepairOrderReplacement> {
    List<EamJigRepairOrderReplacementDto> findList(Map<String, Object> map);
}