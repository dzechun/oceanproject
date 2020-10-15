package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtRouteMapper;
import com.fantechs.provider.imes.basic.service.SmtHtRouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class SmtHtRouteServiceImpl extends BaseService<SmtHtRoute> implements SmtHtRouteService {

    @Resource
    private SmtHtRouteMapper smtHtRouteMapper;

    @Override
    public List<SmtHtRoute> findList(SearchSmtRoute searchSmtRoute) {
        return smtHtRouteMapper.findList(searchSmtRoute);
    }
}
