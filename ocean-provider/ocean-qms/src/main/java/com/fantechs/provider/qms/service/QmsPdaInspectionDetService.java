package com.fantechs.provider.qms.service;


import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDetDto;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspectionDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */

public interface QmsPdaInspectionDetService extends IService<QmsPdaInspectionDet> {

    List<QmsPdaInspectionDetDto> findList(Map<String, Object> map);
}
