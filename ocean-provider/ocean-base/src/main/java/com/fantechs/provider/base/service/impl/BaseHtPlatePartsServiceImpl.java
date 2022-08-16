package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPlatePartsMapper;
import com.fantechs.provider.base.service.BaseHtPlatePartsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@Service
public class BaseHtPlatePartsServiceImpl  extends BaseService<BaseHtPlateParts> implements BaseHtPlatePartsService {

    @Resource
    private BaseHtPlatePartsMapper baseHtPlatePartsMapper;

    @Override
    public List< BaseHtPlateParts> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtPlatePartsMapper.findHtList(map);
    }
}
