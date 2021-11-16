package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.dto.eam.imports.EamJigMaterialImport;
import com.fantechs.common.base.general.entity.eam.EamJigMaterial;
import com.fantechs.common.base.support.IService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamJigMaterialService extends IService<EamJigMaterial> {
    List<EamJigMaterialDto> findList(Map<String, Object> map);

    int save(EamJigMaterialDto record);

    int update(EamJigMaterialDto record);

    Map<String, Object> importExcel(List<EamJigMaterialImport> eamJigMaterialImports) throws ParseException;
}
