package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamReturnOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamReturnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamReturnOrderDetMapper;
import com.fantechs.provider.eam.service.EamReturnOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2021/06/29.
 */
@Service
public class EamReturnOrderDetServiceImpl extends BaseService<EamReturnOrderDet> implements EamReturnOrderDetService {

    @Resource
    private EamReturnOrderDetMapper eamReturnOrderDetMapper;

    @Override
    public List<EamReturnOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamReturnOrderDetMapper.findList(map);
    }
}
