package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsInspectionTypeDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionType;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */

public interface QmsInspectionTypeService extends IService<QmsInspectionType> {

    List<QmsInspectionTypeDto> findList(Map<String, Object> map);
}
