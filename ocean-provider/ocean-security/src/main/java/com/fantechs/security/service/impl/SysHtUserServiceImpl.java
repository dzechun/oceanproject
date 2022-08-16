package com.fantechs.security.service.impl;

import com.fantechs.common.base.entity.security.history.SysHtUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.security.mapper.SysHtUserMapper;
import com.fantechs.security.service.SysHtUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysHtUserServiceImpl extends BaseService<SysHtUser> implements SysHtUserService {

    @Resource
    private SysHtUserMapper sysHtUserMapper;

    @Override
    public List<SysHtUser> selectHtUsers(SearchSysUser searchSysUser) {
        return sysHtUserMapper.selectHtUsers(searchSysUser);
    }
}
