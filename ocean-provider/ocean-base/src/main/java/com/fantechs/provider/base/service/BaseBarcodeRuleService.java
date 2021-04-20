package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/26.
 */

public interface BaseBarcodeRuleService extends IService<BaseBarcodeRule> {

    List<BaseBarcodeRuleDto> findList(SearchBaseBarcodeRule searchBaseBarcodeRule);

    int preserve(BaseBarcodeRule baseBarcodeRule);
}