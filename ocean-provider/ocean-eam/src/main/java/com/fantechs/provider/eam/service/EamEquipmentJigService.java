package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentJigDto;
import com.fantechs.common.base.general.dto.eam.imports.EamEquipmentJigImport;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJig;
import com.fantechs.common.base.support.IService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */

public interface EamEquipmentJigService extends IService<EamEquipmentJig> {
    List<EamEquipmentJigDto> findList(Map<String, Object> map);

    int save(EamEquipmentJigDto record);

    int update(EamEquipmentJigDto entity);

    Map<String, Object> importExcel(List<EamEquipmentJigImport> eamEquipmentJigImports) throws ParseException;
}
