package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionExemptedList;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/21.
 */

public interface BaseHtInspectionExemptedListService extends IService<BaseHtInspectionExemptedList> {
    List<BaseHtInspectionExemptedList> findHtList(Map<String, Object> map);
}
