package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtWarehouseMapper;
import com.fantechs.provider.base.service.BaseHtWarehouseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/23.
 */
@Service
public class BaseHtWarehouseServiceImpl extends BaseService<BaseHtWarehouse> implements BaseHtWarehouseService {

    @Resource
    private BaseHtWarehouseMapper baseHtWarehouseMapper;

    @Override
    public List<BaseHtWarehouse> findHtList(SearchBaseWarehouse searchBaseWarehouse) {
        return baseHtWarehouseMapper.findHtList(searchBaseWarehouse);
    }
}
