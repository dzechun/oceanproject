package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsQualityInspectionDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */

public interface QmsQualityInspectionDetService extends IService<QmsQualityInspectionDet> {

    List<QmsQualityInspectionDet> findList(Map<String, Object> map);
}
