package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseDept;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtDeptMapper extends MyMapper<BaseHtDept> {
    //int addBatchHtDept(List<SmtHtDept> list);

    List<BaseHtDept> selectHtDepts(SearchBaseDept searchBaseDept);
}