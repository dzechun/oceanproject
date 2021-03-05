package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsFirstInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsFirstInspection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/06.
 */

public interface QmsFirstInspectionService extends IService<QmsFirstInspection> {

    List<QmsFirstInspectionDto> findList(Map<String, Object> map);

}
