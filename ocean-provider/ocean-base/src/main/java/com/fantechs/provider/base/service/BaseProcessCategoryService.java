package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseProcessCategoryDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProcessCategoryImport;
import com.fantechs.common.base.general.entity.basic.BaseProcessCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/10/15.
 */

public interface BaseProcessCategoryService extends IService<BaseProcessCategory> {

    List<BaseProcessCategoryDto> findList(Map<String,Object> map);
    Map<String, Object> importExcel(List<BaseProcessCategoryImport> baseProcessCategoryImportList);

    BaseProcessCategory addOrUpdate (BaseProcessCategory baseProcessCategory);
}
