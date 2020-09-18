package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductModelMapper;
import com.fantechs.provider.imes.basic.service.SmtHtProductModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmtHtProductModelServiceImpl extends BaseService<SmtHtProductModel> implements SmtHtProductModelService {

    @Resource
    private SmtHtProductModelMapper smtHtProductModelMapper;

    @Override
    public List<SmtHtProductModel> selectHtProductModels(SearchSmtProductModel searchSmtProductModel) {
        return smtHtProductModelMapper.selectHtProductModels(searchSmtProductModel);
    }
}
