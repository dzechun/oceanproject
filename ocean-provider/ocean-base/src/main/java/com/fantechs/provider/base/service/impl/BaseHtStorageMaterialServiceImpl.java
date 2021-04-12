package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtStorageMaterialMapper;
import com.fantechs.provider.base.service.BaseHtStorageMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@Service
public class BaseHtStorageMaterialServiceImpl extends BaseService<BaseHtStorageMaterial> implements BaseHtStorageMaterialService {

    @Resource
    private BaseHtStorageMaterialMapper baseHtStorageMaterialMapper;

    @Override
    public List<BaseHtStorageMaterial> findHtList(SearchBaseStorageMaterial searchBaseStorageMaterial) {
        return baseHtStorageMaterialMapper.findHtList(searchBaseStorageMaterial);
    }
}
