package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPartsInformation;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPartsInformationMapper;
import com.fantechs.provider.base.service.BaseHtPartsInformationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/14.
 */
@Service
public class BaseHtPartsInformationServiceImpl  extends BaseService<BaseHtPartsInformation> implements BaseHtPartsInformationService {

    @Resource
    private BaseHtPartsInformationMapper baseHtPartsInformationMapper;

    @Override
    public List<BaseHtPartsInformation> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtPartsInformationMapper.findHtList(map);
    }
}
