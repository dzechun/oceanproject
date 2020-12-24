package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionType;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */

public interface QmsHtInspectionTypeService extends IService<QmsHtInspectionType> {

    List<QmsHtInspectionType> findHtList(Map<String, Object> map);
}
