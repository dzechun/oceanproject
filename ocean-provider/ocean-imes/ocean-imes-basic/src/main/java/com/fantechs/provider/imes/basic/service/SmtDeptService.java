package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.imports.SmtDeptImport;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.support.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SmtDeptService extends IService<SmtDept> {
    //根据条件查询部门信息列表
    List<SmtDept> findList(SearchSmtDept searchSmtDept);

    Map<String, Object> importExcel(List<SmtDeptImport> smtDeptImports);

}
