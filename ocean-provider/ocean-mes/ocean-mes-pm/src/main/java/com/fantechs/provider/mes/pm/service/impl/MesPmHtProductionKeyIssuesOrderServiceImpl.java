package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProductionKeyIssuesOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmHtProductionKeyIssuesOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmHtProductionKeyIssuesOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */
@Service
public class MesPmHtProductionKeyIssuesOrderServiceImpl extends BaseService<MesPmHtProductionKeyIssuesOrder> implements MesPmHtProductionKeyIssuesOrderService {

    @Resource
    private MesPmHtProductionKeyIssuesOrderMapper mesPmHtProductionKeyIssuesOrderMapper;

    @Override
    public List<MesPmHtProductionKeyIssuesOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orgId", user.getOrganizationId());
        return mesPmHtProductionKeyIssuesOrderMapper.findHtList(map);
    }
}
