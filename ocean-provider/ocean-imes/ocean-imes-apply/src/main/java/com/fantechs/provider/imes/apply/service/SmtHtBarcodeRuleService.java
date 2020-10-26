package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.entity.apply.SmtBarcodeRule;
import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRule;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/26.
 */

public interface SmtHtBarcodeRuleService extends IService<SmtHtBarcodeRule> {

    List<SmtBarcodeRule> findList(SearchSmtBarcodeRule searchSmtBarcodeRule);
}
