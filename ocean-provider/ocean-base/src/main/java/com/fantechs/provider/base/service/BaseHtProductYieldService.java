package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductYield;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/20.
 */

public interface BaseHtProductYieldService extends IService<BaseHtProductYield> {
    List<BaseHtProductYield> findHtList(Map<String, Object> map);
}
