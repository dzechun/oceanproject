package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopWorkInstructionDto;
import com.fantechs.common.base.general.entity.esop.EsopWorkInstruction;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWorkInstruction;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EsopWorkInstructionMapper extends MyMapper<EsopWorkInstruction> {
    List<EsopWorkInstructionDto> findList(SearchEsopWorkInstruction searchEsopWorkInstruction);

}