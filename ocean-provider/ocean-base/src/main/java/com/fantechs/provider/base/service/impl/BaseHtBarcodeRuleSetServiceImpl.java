package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtBarcodeRuleSetMapper;
import com.fantechs.provider.base.service.BaseHtBarcodeRuleSetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/09.
 */
@Service
public class BaseHtBarcodeRuleSetServiceImpl extends BaseService<BaseHtBarcodeRuleSet> implements BaseHtBarcodeRuleSetService {

        @Resource
        private BaseHtBarcodeRuleSetMapper baseHtBarcodeRuleSetMapper;

        @Override
        public List<BaseHtBarcodeRuleSet> findList(SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet) {
            return baseHtBarcodeRuleSetMapper.findList(searchBaseBarcodeRuleSet);
        }
}
