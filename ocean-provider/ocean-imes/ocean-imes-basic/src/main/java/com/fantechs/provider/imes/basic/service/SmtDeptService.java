package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.sysmanage.SmtDept;
import com.fantechs.common.base.entity.sysmanage.search.SearchSmtDept;

import java.util.List;

public interface SmtDeptService {
    //根据条件查询部门信息列表
    List<SmtDept> selectDepts(SearchSmtDept searchSmtDept);

    //新增部门信息
    int insert(SmtDept smtDept);

    //修改部门信息1
    int updateById(SmtDept smtDept);

    //删除部门信息
    int deleteByIds(List<String> deptIds);

    //导出部门信息
    List<SmtDept> exportDepts(SearchSmtDept searchSmtDept);

    List<SmtDept> selectDeptByFactoryId(String factoryId);
}
