package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSampleStandardDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleStandard;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSampleStandardMapper extends MyMapper<BaseSampleStandard> {

    List<BaseSampleStandardDto> findList(Map<String, Object> map);
}