package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamWorkInstructionDto;
import com.fantechs.common.base.general.entity.eam.EamWorkInstruction;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWorkInstruction;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EamWorkInstructionMapper extends MyMapper<EamWorkInstruction> {
    List<EamWorkInstructionDto> findList(SearchEamWorkInstruction searchEamWorkInstruction);

}