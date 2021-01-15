package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSetDet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSetDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/10.
 */

public interface SmtBarcodeRuleSetDetService extends IService<SmtBarcodeRuleSetDet> {

    List<SmtBarcodeRuleSetDetDto> findList(SearchSmtBarcodeRuleSetDet searchSmtBarcodeRuleSetDet);

    //绑定条码规则
    int bindBarcodeRule(Long barcodeRuleSetId, List<Long> barcodeRuleIds);
}
