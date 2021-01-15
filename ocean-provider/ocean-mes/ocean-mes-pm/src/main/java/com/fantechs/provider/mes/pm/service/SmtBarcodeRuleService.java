package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRule;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRule;
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
