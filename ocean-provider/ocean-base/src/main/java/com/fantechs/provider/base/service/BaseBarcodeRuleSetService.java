package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/09.
 */

public interface BaseBarcodeRuleSetService extends IService<BaseBarcodeRuleSet> {

    List<BaseBarcodeRuleSetDto> findList(SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet);
}
