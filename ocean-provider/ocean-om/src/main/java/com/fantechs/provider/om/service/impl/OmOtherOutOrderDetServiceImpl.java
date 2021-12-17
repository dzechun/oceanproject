package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.om.mapper.OmOtherOutOrderDetMapper;
import com.fantechs.provider.om.service.OmOtherOutOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/06/23.
 */
@Service
public class OmOtherOutOrderDetServiceImpl extends BaseService<OmOtherOutOrderDet> implements OmOtherOutOrderDetService {

    @Resource
    private OmOtherOutOrderDetMapper omOtherOutOrderDetMapper;

    @Override
    public List<OmOtherOutOrderDetDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omOtherOutOrderDetMapper.findList(map);
    }
}
