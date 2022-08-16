package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionType;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */

public interface BaseHtInspectionTypeService extends IService<BaseHtInspectionType> {

    List<BaseHtInspectionType> findHtList(Map<String, Object> map);
}
