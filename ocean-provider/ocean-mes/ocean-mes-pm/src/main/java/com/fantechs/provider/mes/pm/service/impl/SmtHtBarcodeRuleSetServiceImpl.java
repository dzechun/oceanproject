package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.SmtHtBarcodeRuleSetMapper;
import com.fantechs.provider.mes.pm.service.SmtHtBarcodeRuleSetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/09.
 */
@Service
public class SmtHtBarcodeRuleSetServiceImpl extends BaseService<SmtHtBarcodeRuleSet> implements SmtHtBarcodeRuleSetService {

        @Resource
        private SmtHtBarcodeRuleSetMapper smtHtBarcodeRuleSetMapper;

        @Override
        public List<SmtHtBarcodeRuleSet> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet) {
            return smtHtBarcodeRuleSetMapper.findList(searchSmtBarcodeRuleSet);
        }
}
