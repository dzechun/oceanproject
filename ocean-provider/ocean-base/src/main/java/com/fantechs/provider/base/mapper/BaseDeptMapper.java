package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseDept;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseDeptMapper extends MyMapper<BaseDept> {
    List<BaseDept> findList(Map<String, Object> map);

    //Long findList_COUNT(Map<String, Object> map);

    List<BaseDept> findById(Long parentId);
}
