package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/09.
 */

public interface SmtHtBarcodeRuleSetService extends IService<SmtHtBarcodeRuleSet> {

    List<SmtHtBarcodeRuleSet> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet);
}
