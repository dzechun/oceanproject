package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPackingUnit;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPackingUnitMapper;
import com.fantechs.provider.base.service.BaseHtPackingUnitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/03.
 */
@Service
public class BaseHtPackingUnitServiceImpl extends BaseService<BaseHtPackingUnit> implements BaseHtPackingUnitService {

    @Resource
    private BaseHtPackingUnitMapper baseHtPackingUnitMapper;

    @Override
    public List<BaseHtPackingUnit> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtPackingUnitMapper.findHtList(map);
    }
}
