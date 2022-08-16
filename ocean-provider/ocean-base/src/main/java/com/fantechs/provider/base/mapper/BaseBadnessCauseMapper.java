package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBadnessCauseDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCause;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseBadnessCauseMapper extends MyMapper<BaseBadnessCause> {

    List<BaseBadnessCauseDto> findList(Map<String, Object> map);
}