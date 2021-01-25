package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSpecDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/07.
 */

public interface SmtBarcodeRuleSpecService extends IService<SmtBarcodeRuleSpec> {

    List<SmtBarcodeRuleSpecDto> findList(SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec);

    List<SmtBarcodeRuleSpec> findSpec(SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec);
}
