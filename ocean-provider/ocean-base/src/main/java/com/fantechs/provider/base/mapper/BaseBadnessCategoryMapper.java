package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBadnessCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseBadnessCategoryMapper extends MyMapper<BaseBadnessCategory> {
    List<BaseBadnessCategoryDto> findList(Map<String, Object> map);
}
