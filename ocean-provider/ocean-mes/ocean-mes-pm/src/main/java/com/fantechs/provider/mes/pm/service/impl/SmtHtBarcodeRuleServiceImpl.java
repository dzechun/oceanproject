package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRule;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.SmtHtBarcodeRuleMapper;
import com.fantechs.provider.mes.pm.service.SmtHtBarcodeRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/26.
 */
@Service
public class SmtHtBarcodeRuleServiceImpl extends BaseService<SmtHtBarcodeRule> implements SmtHtBarcodeRuleService {

     @Resource
     private SmtHtBarcodeRuleMapper smtHtBarcodeRuleMapper;

     @Override
     public List<SmtHtBarcodeRule> findList(SearchSmtBarcodeRule searchSmtBarcodeRule) {
        return smtHtBarcodeRuleMapper.findList(searchSmtBarcodeRule);
     }
}
