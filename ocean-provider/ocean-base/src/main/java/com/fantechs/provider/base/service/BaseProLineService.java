package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.dto.basic.imports.BaseProLineImport;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface BaseProLineService extends IService<BaseProLine> {
    //根据条件查询生产线信息列表
    List<BaseProLine> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseProLineImport> baseProLineImports);

    BaseProLine addOrUpdate (BaseProLine baseProLine);

    List<BaseProLine> batchAdd(List<BaseProLine> baseProLines);
}
