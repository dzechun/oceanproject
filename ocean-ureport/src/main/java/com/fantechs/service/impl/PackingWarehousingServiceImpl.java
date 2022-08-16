package com.fantechs.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.entity.PackingWarehousingModel;
import com.fantechs.mapper.PackingWarehousingMapper;
import com.fantechs.service.PackingWarehousingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
@Service
public class PackingWarehousingServiceImpl implements PackingWarehousingService {

    @Resource
    private PackingWarehousingMapper packingWarehousingMapper;

    @Override
    public List<PackingWarehousingModel> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return packingWarehousingMapper.findList(map);
    }
}
