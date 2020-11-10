package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleSetDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/09.
 */

public interface SmtBarcodeRuleSetService extends IService<SmtBarcodeRuleSet> {

    List<SmtBarcodeRuleSetDto> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet);
}
