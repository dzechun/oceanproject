package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */

public interface QmsQualityInspectionService extends IService<QmsQualityInspection> {

    List<QmsQualityInspectionDto> findList(Map<String, Object> map);
}
