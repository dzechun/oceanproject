package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtProcessCategory;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SmtHtProcessCategoryMapper extends MyMapper<SmtHtProcessCategory> {

    List<SmtHtProcessCategory> findHtList(Map<String,Object> map);
}