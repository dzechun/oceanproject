package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtWarehouseArea;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtWarehouseAreaMapper;
import com.fantechs.provider.imes.basic.service.SmtHtWarehouseAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */
@Service
public class SmtHtWarehouseAreaServiceImpl  extends BaseService<SmtHtWarehouseArea> implements SmtHtWarehouseAreaService {

    @Autowired
    private SmtHtWarehouseAreaMapper smtHtWarehouseAreaMapper;
    @Override
    public List<SmtHtWarehouseArea> findHtList(Map<String, Object> map) {
        return smtHtWarehouseAreaMapper.findHtList(map);
    }
}
