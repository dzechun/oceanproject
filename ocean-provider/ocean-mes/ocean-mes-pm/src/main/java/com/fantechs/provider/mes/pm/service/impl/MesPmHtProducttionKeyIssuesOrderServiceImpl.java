package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProducttionKeyIssuesOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmHtProducttionKeyIssuesOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmHtProducttionKeyIssuesOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */
@Service
public class MesPmHtProducttionKeyIssuesOrderServiceImpl extends BaseService<MesPmHtProducttionKeyIssuesOrder> implements MesPmHtProducttionKeyIssuesOrderService {

    @Resource
    private MesPmHtProducttionKeyIssuesOrderMapper mesPmHtProducttionKeyIssuesOrderMapper;

    @Override
    public List<MesPmHtProducttionKeyIssuesOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orgId", user.getOrganizationId());
        return mesPmHtProducttionKeyIssuesOrderMapper.findHtList(map);
    }
}
