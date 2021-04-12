package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtStationMapper;
import com.fantechs.provider.base.service.BaseHtStationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/27.
 */
@Service
public class BaseHtStationServiceImpl extends BaseService<BaseHtStation> implements BaseHtStationService {

    @Resource
    private BaseHtStationMapper baseHtStationMapper;

    @Override
    public List<BaseHtStation> findList(SearchBaseStation searchBaseStation) {
        return baseHtStationMapper.findList(searchBaseStation);
    }
}
