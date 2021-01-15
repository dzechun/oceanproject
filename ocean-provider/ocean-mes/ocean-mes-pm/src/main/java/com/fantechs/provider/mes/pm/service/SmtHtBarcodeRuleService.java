package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtBarcodeRule;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/26.
 */

public interface SmtHtBarcodeRuleService extends IService<SmtHtBarcodeRule> {

    List<SmtHtBarcodeRule> findList(SearchSmtBarcodeRule searchSmtBarcodeRule);
}
