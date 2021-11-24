package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRepairOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigRepairOrderMapper;
import com.fantechs.provider.eam.service.EamHtJigRepairOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */
@Service
public class EamHtJigRepairOrderServiceImpl extends BaseService<EamHtJigRepairOrder> implements EamHtJigRepairOrderService {

    @Resource
    private EamHtJigRepairOrderMapper eamHtJigRepairOrderMapper;

    @Override
    public List<EamHtJigRepairOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamHtJigRepairOrderMapper.findHtList(map);
    }
}
