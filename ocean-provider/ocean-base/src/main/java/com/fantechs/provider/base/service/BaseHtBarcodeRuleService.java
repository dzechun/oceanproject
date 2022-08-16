package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/26.
 */

public interface BaseHtBarcodeRuleService extends IService<BaseHtBarcodeRule> {

    List<BaseHtBarcodeRule> findList(Map<String, Object> map);
}
