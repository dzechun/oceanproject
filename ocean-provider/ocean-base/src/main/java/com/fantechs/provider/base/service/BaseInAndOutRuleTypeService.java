package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleType;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/14.
 */

public interface BaseInAndOutRuleTypeService extends IService<BaseInAndOutRuleType> {
    List<BaseInAndOutRuleType> findList(Map<String, Object> map);
}
