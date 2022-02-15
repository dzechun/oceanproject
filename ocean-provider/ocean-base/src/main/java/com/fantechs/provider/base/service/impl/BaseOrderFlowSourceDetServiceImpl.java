package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlowSourceDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.base.mapper.BaseOrderFlowSourceDetMapper;
import com.fantechs.provider.base.service.BaseOrderFlowSourceDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/02/15.
 */
@Service
public class BaseOrderFlowSourceDetServiceImpl extends BaseService<BaseOrderFlowSourceDet> implements BaseOrderFlowSourceDetService {

    @Resource
    private BaseOrderFlowSourceDetMapper baseOrderFlowSourceDetMapper;

    @Override
    public List<BaseOrderFlowSourceDet> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseOrderFlowSourceDetMapper.findList(map);
    }
}
