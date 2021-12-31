package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseHtInAndOutRuleDetDto;
import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/30.
 */

public interface BaseInAndOutRuleDetService extends IService<BaseInAndOutRuleDet> {
    List<BaseInAndOutRuleDetDto> findList(Map<String, Object> map);

    List<BaseHtInAndOutRuleDetDto> findHtList(Map<String,Object> map);
}
