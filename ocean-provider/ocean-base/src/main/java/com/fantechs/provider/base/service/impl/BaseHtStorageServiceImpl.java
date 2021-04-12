package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtStorageMapper;
import com.fantechs.provider.base.service.BaseHtStorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/23.
 */
@Service
public class BaseHtStorageServiceImpl extends BaseService<BaseHtStorage> implements BaseHtStorageService {

    @Resource
    private BaseHtStorageMapper baseHtStorageMapper;

    @Override
    public List<BaseHtStorage> findHtList(SearchBaseStorage searchBaseStorage) {
        return baseHtStorageMapper.findHtList(searchBaseStorage);
    }
}
