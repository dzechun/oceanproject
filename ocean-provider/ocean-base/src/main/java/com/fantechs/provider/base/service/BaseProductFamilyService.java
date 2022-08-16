package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductFamilyImport;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/15.
 */

public interface BaseProductFamilyService extends IService<BaseProductFamily> {

    List<BaseProductFamilyDto> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseProductFamilyImport> baseProductFamilyImports);
}
