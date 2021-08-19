package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigScrapOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigScrapOrderMapper;
import com.fantechs.provider.eam.service.EamHtJigScrapOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/19.
 */
@Service
public class EamHtJigScrapOrderServiceImpl extends BaseService<EamHtJigScrapOrder> implements EamHtJigScrapOrderService {

    @Resource
    private EamHtJigScrapOrderMapper eamHtJigScrapOrderMapper;

    @Override
    public List<EamHtJigScrapOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamHtJigScrapOrderMapper.findHtList(map);
    }
}
