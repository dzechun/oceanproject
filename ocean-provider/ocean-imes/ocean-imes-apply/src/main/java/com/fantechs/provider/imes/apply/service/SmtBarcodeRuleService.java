package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRule;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/26.
 */

public interface SmtBarcodeRuleService extends IService<SmtBarcodeRule> {

    List<SmtBarcodeRuleDto> findList(SearchSmtBarcodeRule searchSmtBarcodeRule);

    int preserve(SmtBarcodeRule smtBarcodeRule);
}
