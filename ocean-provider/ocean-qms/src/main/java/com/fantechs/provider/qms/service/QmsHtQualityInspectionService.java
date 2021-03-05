package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.history.QmsHtQualityInspection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/16.
 */

public interface QmsHtQualityInspectionService extends IService<QmsHtQualityInspection> {

    List<QmsHtQualityInspection> findHtList(Map<String, Object> map);
}
