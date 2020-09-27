package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.SmtStation;
import com.fantechs.common.base.entity.basic.history.SmtHtStation;
import com.fantechs.common.base.entity.basic.search.SearchSmtStation;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtStationMapper;
import com.fantechs.provider.imes.basic.service.SmtHtStationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/27.
 */
@Service
public class SmtHtStationServiceImpl  extends BaseService<SmtHtStation> implements SmtHtStationService {

    @Resource
    private SmtHtStationMapper smtHtStationMapper;

    @Override
    public List<SmtStation> findList(SearchSmtStation searchSmtStation) {
        return smtHtStationMapper.findList(searchSmtStation);
    }
}
