package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtBarcodeRuleSet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/09.
 */

public interface SmtHtBarcodeRuleSetService extends IService<SmtHtBarcodeRuleSet> {

    List<SmtHtBarcodeRuleSet> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet);
}
