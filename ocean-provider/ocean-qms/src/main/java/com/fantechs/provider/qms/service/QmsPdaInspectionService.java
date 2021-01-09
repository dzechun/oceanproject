package com.fantechs.provider.qms.service;


import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */

public interface QmsPdaInspectionService extends IService<QmsPdaInspection> {

    List<QmsPdaInspectionDto> findList(Map<String, Object> map);

    QmsPdaInspectionDto analysisCode(Map<String, Object> map);
}
