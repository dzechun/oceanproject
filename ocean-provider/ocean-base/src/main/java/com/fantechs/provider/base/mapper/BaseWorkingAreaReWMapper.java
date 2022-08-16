package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaReWDto;
import com.fantechs.common.base.general.entity.basic.BaseWorkingAreaReW;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseWorkingAreaReWMapper extends MyMapper<BaseWorkingAreaReW> {
    List<BaseWorkingAreaReWDto> findList(Map<String, Object> map);
}