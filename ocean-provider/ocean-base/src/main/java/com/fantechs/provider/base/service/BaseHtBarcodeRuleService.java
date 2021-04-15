package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/26.
 */

public interface BaseHtBarcodeRuleService extends IService<BaseHtBarcodeRule> {

    List<BaseHtBarcodeRule> findList(SearchBaseBarcodeRule searchBaseBarcodeRule);
}
