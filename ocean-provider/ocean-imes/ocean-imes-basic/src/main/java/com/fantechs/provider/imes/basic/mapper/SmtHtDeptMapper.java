package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.history.SmtHtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtDeptMapper extends MyMapper<SmtHtDept> {
    //int addBatchHtDept(List<SmtHtDept> list);

    List<SmtHtDept> selectHtDepts(SearchSmtDept searchSmtDept);
}