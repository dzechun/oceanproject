package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigPointInspectionOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigPointInspectionOrderMapper;
import com.fantechs.provider.eam.service.EamHtJigPointInspectionOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */
@Service
public class EamHtJigPointInspectionOrderServiceImpl extends BaseService<EamHtJigPointInspectionOrder> implements EamHtJigPointInspectionOrderService {

    @Resource
    private EamHtJigPointInspectionOrderMapper eamHtJigPointInspectionOrderMapper;

    @Override
    public List<EamHtJigPointInspectionOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamHtJigPointInspectionOrderMapper.findHtList(map);
    }
}
