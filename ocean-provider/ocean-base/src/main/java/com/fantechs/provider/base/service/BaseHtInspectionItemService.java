package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/03.
 */

public interface BaseHtInspectionItemService extends IService<BaseHtInspectionItem> {
    List<BaseHtInspectionItem> findHtList(Map<String, Object> map);
}
