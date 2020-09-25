package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.support.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SmtDeptService extends IService<SmtDept> {
    //根据条件查询部门信息列表
    List<SmtDept> selectDepts(SearchSmtDept searchSmtDept);

    //删除部门信息
    int deleteByIds(List<Long> deptIds);
}
