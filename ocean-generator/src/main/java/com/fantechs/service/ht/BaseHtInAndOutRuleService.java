package com.fantechs.service.ht;

import com.fantechs.model.BaseHtInAndOutRule;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/30.
 */

public interface BaseHtInAndOutRuleService extends IService<BaseHtInAndOutRule> {
    List<BaseHtInAndOutRuleDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseHtInAndOutRule> list);
}
