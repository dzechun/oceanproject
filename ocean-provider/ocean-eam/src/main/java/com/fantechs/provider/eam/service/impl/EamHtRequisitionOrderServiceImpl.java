package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtRequisitionOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtRequisitionOrderMapper;
import com.fantechs.provider.eam.service.EamHtRequisitionOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */
@Service
public class EamHtRequisitionOrderServiceImpl extends BaseService<EamHtRequisitionOrder> implements EamHtRequisitionOrderService {

    @Resource
    private EamHtRequisitionOrderMapper eamHtRequisitionOrderMapper;

    @Override
    public List<EamHtRequisitionOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamHtRequisitionOrderMapper.findHtList(map);
    }
}
