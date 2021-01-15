package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSetDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/09.
 */

public interface SmtBarcodeRuleSetService extends IService<SmtBarcodeRuleSet> {

    List<SmtBarcodeRuleSetDto> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet);
}
