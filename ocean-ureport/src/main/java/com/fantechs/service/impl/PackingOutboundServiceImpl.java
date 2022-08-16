package com.fantechs.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.entity.PackingOutboundModel;
import com.fantechs.mapper.PackingOutboundMapper;
import com.fantechs.service.PackingOutboundService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
@Service
public class PackingOutboundServiceImpl implements PackingOutboundService {

    @Resource
    private PackingOutboundMapper packingOutboundMapper;

    @Override
    public List<PackingOutboundModel> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return packingOutboundMapper.findList(map);
    }
}
