package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductBomImport;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface BaseProductBomService extends IService<BaseProductBom> {

    List<BaseProductBomDto> findList(Map<String,Object> map);
    Map<String, Object> importExcel(List<BaseProductBomImport> baseProductBomImports);

    BaseProductBom addOrUpdate (BaseProductBom baseProductBom);

    BaseProductBomDto findNextLevelProductBomDet(SearchBaseProductBom searchBaseProductBom);
}
