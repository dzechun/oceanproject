package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtOrderType;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/26.
 */

public interface BaseHtOrderTypeService extends IService<BaseHtOrderType> {
    List<BaseHtOrderType> findHtList(Map<String, Object> map);
}
