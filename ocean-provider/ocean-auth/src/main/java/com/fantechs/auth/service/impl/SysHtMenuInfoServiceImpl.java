package com.fantechs.auth.service.impl;

import com.fantechs.common.base.entity.security.history.SysHtMenuInfo;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.auth.mapper.SysHtMenuInfoMapper;
import com.fantechs.auth.service.SysHtMenuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysHtMenuInfoServiceImpl extends BaseService<SysHtMenuInfo> implements SysHtMenuInfoService {

    @Autowired
    private SysHtMenuInfoMapper sysHtMenuInfoMapper;

    @Override
    public List<SysHtMenuInfo> findHtList(Map<String, Object> map) {
        return sysHtMenuInfoMapper.findHtList(map);
    }
}
