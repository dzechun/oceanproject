package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.history.SmtHtProductProcessRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductProcessRouteMapper;
import com.fantechs.provider.imes.basic.service.SmtHtProductProcessRouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/30.
 */
@Service
public class SmtHtProductProcessRouteServiceImpl extends BaseService<SmtHtProductProcessRoute> implements SmtHtProductProcessRouteService {

    @Resource
    private SmtHtProductProcessRouteMapper smtHtProductProcessRouteMapper;

    @Override
    public List<SmtProductProcessRoute> findList(SearchSmtProductProcessRoute searchSmtProductProcessRoute) {
        return smtHtProductProcessRouteMapper.findList(searchSmtProductProcessRoute);
    }
}
