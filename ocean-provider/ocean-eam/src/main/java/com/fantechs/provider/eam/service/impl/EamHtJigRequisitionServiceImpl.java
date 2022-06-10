package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigRequisitionMapper;
import com.fantechs.provider.eam.service.EamHtJigRequisitionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/30.
 */
@Service
public class EamHtJigRequisitionServiceImpl extends BaseService<EamHtJigRequisition> implements EamHtJigRequisitionService {

    @Resource
    private EamHtJigRequisitionMapper eamHtJigRequisitionMapper;

    @Override
    public List<EamHtJigRequisition> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamHtJigRequisitionMapper.findHtList(map);
    }
}