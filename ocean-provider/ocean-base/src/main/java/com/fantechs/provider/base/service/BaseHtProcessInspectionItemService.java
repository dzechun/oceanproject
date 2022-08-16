package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessInspectionItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */

public interface BaseHtProcessInspectionItemService extends IService<BaseHtProcessInspectionItem> {
    List<BaseHtProcessInspectionItem> findHtList(Map<String, Object> map);
}
