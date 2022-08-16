package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkShiftImport;
import com.fantechs.common.base.general.entity.basic.BaseWorkShift;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/21.
 */

public interface BaseWorkShiftService extends IService<BaseWorkShift> {

    List<BaseWorkShiftDto> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseWorkShiftImport> baseWorkShiftImports);
}
