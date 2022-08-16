package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseLabelCategoryMapper extends MyMapper<BaseLabelCategory> {
    List<BaseLabelCategoryDto> findList(Map<String, Object> map);
}