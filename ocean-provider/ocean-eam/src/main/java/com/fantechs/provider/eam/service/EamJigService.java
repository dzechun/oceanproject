package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigDto;
import com.fantechs.common.base.general.dto.eam.imports.EamJigImport;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamJigService extends IService<EamJig> {
    List<EamJigDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<EamJigImport> eamJigImports);
}
