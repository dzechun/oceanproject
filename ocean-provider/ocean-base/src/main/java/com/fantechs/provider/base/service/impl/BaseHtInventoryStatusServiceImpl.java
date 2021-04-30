package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInventoryStatus;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtInventoryStatusMapper;
import com.fantechs.provider.base.service.BaseHtInventoryStatusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseHtInventoryStatusServiceImpl extends BaseService<BaseHtInventoryStatus> implements BaseHtInventoryStatusService {

    @Resource
    private BaseHtInventoryStatusMapper baseHtInventoryStatusMapper;

    @Override
    public List<BaseHtInventoryStatus> findHtList(Map<String, Object> map) {
        return baseHtInventoryStatusMapper.findHtList(map);
    }
}
