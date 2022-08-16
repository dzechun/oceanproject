package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaDto;
import com.fantechs.common.base.general.entity.basic.BaseWorkingArea;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseWorkingAreaMapper extends MyMapper<BaseWorkingArea> {
    List<BaseWorkingAreaDto> findList(Map<String, Object> map);
}
