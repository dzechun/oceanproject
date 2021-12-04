package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigMaintainOrderMapper;
import com.fantechs.provider.eam.service.EamHtJigMaintainOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/13.
 */
@Service
public class EamHtJigMaintainOrderServiceImpl extends BaseService<EamHtJigMaintainOrder> implements EamHtJigMaintainOrderService {

    @Resource
    private EamHtJigMaintainOrderMapper eamHtJigMaintainOrderMapper;

    @Override
    public List<EamHtJigMaintainOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamHtJigMaintainOrderMapper.findHtList(map);
    }
}
