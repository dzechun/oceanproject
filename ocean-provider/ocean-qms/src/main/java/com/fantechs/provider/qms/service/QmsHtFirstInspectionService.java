package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.history.QmsHtFirstInspection;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */

public interface QmsHtFirstInspectionService extends IService<QmsHtFirstInspection> {

    List<QmsHtFirstInspection> findHtList(Map<String, Object> map);
}
