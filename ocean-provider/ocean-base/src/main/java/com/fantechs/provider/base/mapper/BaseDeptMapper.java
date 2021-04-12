package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseDept;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseDeptMapper extends MyMapper<BaseDept> {
    List<BaseDept> findList(SearchBaseDept searchBaseDept);

    List<BaseDept> findById(Long parentId);
}
