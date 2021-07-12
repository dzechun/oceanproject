package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamHtWorkInstructionDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtWorkInstruction;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtWorkInstructionMapper extends MyMapper<EamHtWorkInstruction> {
    List<EamHtWorkInstructionDto> findHtList(Map<String,Object> map);
}