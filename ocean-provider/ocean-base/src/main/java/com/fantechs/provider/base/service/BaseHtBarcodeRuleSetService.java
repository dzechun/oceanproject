package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/11/09.
 */

public interface BaseHtBarcodeRuleSetService extends IService<BaseHtBarcodeRuleSet> {

    List<BaseHtBarcodeRuleSet> findList(Map<String, Object> map);
}
