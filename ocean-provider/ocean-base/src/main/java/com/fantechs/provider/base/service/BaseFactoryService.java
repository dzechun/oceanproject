package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseFactoryImport;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface BaseFactoryService extends IService<BaseFactory>{
    List<BaseFactoryDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseFactoryImport> baseFactoryImports);
}
