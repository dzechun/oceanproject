package com.fantechs.security.service.impl;


import com.fantechs.common.base.entity.security.history.SysHtSpecItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.security.mapper.SysHtSpecItemMapper;
import com.fantechs.security.service.SysHtSpecItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SysHtSpecItemServiceImpl extends BaseService<SysHtSpecItem> implements SysHtSpecItemService {
    @Resource
    private SysHtSpecItemMapper SysHtSpecItemMapper;

    @Override
    public List<SysHtSpecItem> findHtSpecItemList(Map<String, Object> map) {
        return SysHtSpecItemMapper.findHtSpecItemList(map);
    }
}
