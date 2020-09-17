package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.sysmanage.history.SmtHtDept;
import com.fantechs.common.base.entity.sysmanage.search.SearchSmtDept;

import java.util.List;

public interface SmtHtDeptService {
    //根据条件查询部门履历信息列表
    List<SmtHtDept> selectHtDepts(SearchSmtDept searchSmtDept);
}
