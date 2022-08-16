package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BasePartsInformationDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePartsInformationImport;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/14.
 */

public interface BasePartsInformationService extends IService<BasePartsInformation> {
    List<BasePartsInformationDto> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BasePartsInformationImport> basePartsInformationImports);
}
