package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSetDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSetDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/10.
 */

public interface BaseBarcodeRuleSetDetService extends IService<BaseBarcodeRuleSetDet> {

    List<BaseBarcodeRuleSetDetDto> findList(SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet);

    //绑定条码规则
    int bindBarcodeRule(Long barcodeRuleSetId, List<Long> barcodeRuleIds);
}
