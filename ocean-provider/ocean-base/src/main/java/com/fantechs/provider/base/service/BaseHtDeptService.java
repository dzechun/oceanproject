package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseDept;

import java.util.List;
import java.util.Map;

public interface BaseHtDeptService {
    //根据条件查询部门履历信息列表
    List<BaseHtDept> selectHtDepts(Map<String, Object> map);
}
