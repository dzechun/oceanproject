package com.fantechs.provider.smt.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.smt.history.SmtHtMaterialLifetime;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.smt.mapper.SmtHtMaterialLifetimeMapper;
import com.fantechs.provider.smt.service.SmtHtMaterialLifetimeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/20.
 */
@Service
public class SmtHtMaterialLifetimeServiceImpl extends BaseService<SmtHtMaterialLifetime> implements SmtHtMaterialLifetimeService {

    @Resource
    private SmtHtMaterialLifetimeMapper smtHtMaterialLifetimeMapper;

    @Override
    public List<SmtHtMaterialLifetime> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return smtHtMaterialLifetimeMapper.findHtList(map);
    }
}
