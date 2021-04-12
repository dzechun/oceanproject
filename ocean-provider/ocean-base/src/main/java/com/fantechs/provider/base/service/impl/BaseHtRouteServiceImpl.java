package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtRouteMapper;
import com.fantechs.provider.base.service.BaseHtRouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class BaseHtRouteServiceImpl extends BaseService<BaseHtRoute> implements BaseHtRouteService {

    @Resource
    private BaseHtRouteMapper baseHtRouteMapper;

    @Override
    public List<BaseHtRoute> findList(SearchBaseRoute searchBaseRoute) {
        return baseHtRouteMapper.findList(searchBaseRoute);
    }
}
