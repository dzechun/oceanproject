package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseProcessInspectionItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */

public interface BaseProcessInspectionItemService extends IService<BaseProcessInspectionItem> {
    List<BaseProcessInspectionItem> findList(Map<String, Object> map);
}
