package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseLabelVariableDto;
import com.fantechs.common.base.general.entity.basic.BaseLabelVariable;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseLabelVariableMapper extends MyMapper<BaseLabelVariable> {
    List<BaseLabelVariableDto> findList(Map<String, Object> map);
}