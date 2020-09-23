package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtWarehouseMapper;
import com.fantechs.provider.imes.basic.service.SmtHtWarehouseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/23.
 */
@Service
public class SmtHtWarehouseServiceImpl extends BaseService<SmtHtWarehouse> implements SmtHtWarehouseService {

    @Resource
    private SmtHtWarehouseMapper smtHtWarehouseMapper;

    @Override
    public List<SmtHtWarehouse> findHtList(SearchSmtWarehouse searchSmtWarehouse) {
        return smtHtWarehouseMapper.findHtList(searchSmtWarehouse);
    }
}
