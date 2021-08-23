package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopHtWorkInstructionDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWorkInstruction;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopHtWorkInstructionMapper extends MyMapper<EsopHtWorkInstruction> {
    List<EsopHtWorkInstructionDto> findHtList(Map<String,Object> map);
}