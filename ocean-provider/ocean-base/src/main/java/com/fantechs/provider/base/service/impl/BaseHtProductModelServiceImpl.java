package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProductModelMapper;
import com.fantechs.provider.base.service.BaseHtProductModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BaseHtProductModelServiceImpl extends BaseService<BaseHtProductModel> implements BaseHtProductModelService {

    @Resource
    private BaseHtProductModelMapper baseHtProductModelMapper;

    @Override
    public List<BaseHtProductModel> selectHtProductModels(SearchBaseProductModel searchBaseProductModel) {
        return baseHtProductModelMapper.selectHtProductModels(searchBaseProductModel);
    }
}
