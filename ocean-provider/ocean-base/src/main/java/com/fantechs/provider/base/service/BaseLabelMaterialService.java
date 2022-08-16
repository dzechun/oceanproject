package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseLabelMaterialImport;
import com.fantechs.common.base.general.entity.basic.BaseLabelMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BaseLabelMaterialService extends IService<BaseLabelMaterial> {
    List<BaseLabelMaterialDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseLabelMaterialImport> baseLabelMaterialImports);
}
