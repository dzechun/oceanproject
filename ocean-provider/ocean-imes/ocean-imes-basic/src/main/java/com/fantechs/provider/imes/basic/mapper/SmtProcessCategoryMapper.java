package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtProcessCategoryDto;
import com.fantechs.common.base.entity.basic.SmtProcessCategory;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SmtProcessCategoryMapper extends MyMapper<SmtProcessCategory> {

    List<SmtProcessCategoryDto> findList(Map<String,Object> map);
}