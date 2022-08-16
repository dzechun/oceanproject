package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.dto.basic.imports.BaseDeptImport;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseDept;
import com.fantechs.common.base.support.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BaseDeptService extends IService<BaseDept> {
    //根据条件查询部门信息列表
    List<BaseDept> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseDeptImport> baseDeptImports);

    List<BaseDept> batchAdd(List<BaseDept> baseDepts);

}
