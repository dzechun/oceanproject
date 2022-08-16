package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseProcessCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseProcessCategory;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseProcessCategoryMapper extends MyMapper<BaseProcessCategory> {

    List<BaseProcessCategoryDto> findList(Map<String,Object> map);
}