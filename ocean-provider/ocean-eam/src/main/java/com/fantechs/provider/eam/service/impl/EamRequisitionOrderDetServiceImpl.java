package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamRequisitionOrderDetMapper;
import com.fantechs.provider.eam.service.EamRequisitionOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */
@Service
public class EamRequisitionOrderDetServiceImpl extends BaseService<EamRequisitionOrderDet> implements EamRequisitionOrderDetService {

    @Resource
    private EamRequisitionOrderDetMapper eamRequisitionOrderDetMapper;

    @Override
    public List<EamRequisitionOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamRequisitionOrderDetMapper.findList(map);
    }
}
