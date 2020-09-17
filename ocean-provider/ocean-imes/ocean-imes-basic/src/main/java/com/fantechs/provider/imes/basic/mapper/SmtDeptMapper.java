package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtDeptMapper extends MyMapper<SmtDept> {
    List<SmtDept> selectDepts(SearchSmtDept searchSmtDept);
}