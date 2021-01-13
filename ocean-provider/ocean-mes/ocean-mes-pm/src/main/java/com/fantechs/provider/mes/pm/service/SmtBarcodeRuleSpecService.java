package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleSpecDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/07.
 */

public interface SmtBarcodeRuleSpecService extends IService<SmtBarcodeRuleSpec> {

    List<SmtBarcodeRuleSpecDto> findList(SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec);

}
