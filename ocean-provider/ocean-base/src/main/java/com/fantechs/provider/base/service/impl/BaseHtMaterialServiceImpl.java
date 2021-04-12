package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtMaterialMapper;
import com.fantechs.provider.base.service.BaseHtMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BaseHtMaterialServiceImpl extends BaseService<BaseHtMaterial> implements BaseHtMaterialService {

    @Resource
    private BaseHtMaterialMapper baseHtMaterialMapper;

    @Override
    public List<BaseHtMaterial> findHtList(SearchBaseMaterial searchBaseMaterial) {
        return baseHtMaterialMapper.findHtList(searchBaseMaterial);
    }
}
