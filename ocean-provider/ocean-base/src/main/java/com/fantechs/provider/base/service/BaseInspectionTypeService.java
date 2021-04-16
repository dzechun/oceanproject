package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseInspectionTypeDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionType;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */

public interface BaseInspectionTypeService extends IService<BaseInspectionType> {

    List<BaseInspectionTypeDto> findList(Map<String, Object> map);

    List<BaseInspectionTypeDto> exportExcel(Map<String, Object> map);
}
