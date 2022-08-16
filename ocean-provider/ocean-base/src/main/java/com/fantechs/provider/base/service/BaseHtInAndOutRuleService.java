package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRule;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/14.
 */

public interface BaseHtInAndOutRuleService extends IService<BaseHtInAndOutRule> {
    List<BaseHtInAndOutRule> findHtList(Map<String, Object> map);
}
