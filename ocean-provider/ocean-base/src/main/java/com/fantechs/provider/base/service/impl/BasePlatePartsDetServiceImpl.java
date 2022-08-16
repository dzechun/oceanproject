package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BasePlatePartsDetMapper;
import com.fantechs.provider.base.service.BasePlatePartsDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@Service
public class BasePlatePartsDetServiceImpl  extends BaseService<BasePlatePartsDet> implements BasePlatePartsDetService {

    @Resource
    private BasePlatePartsDetMapper basePlatePartsDetMapper;

    @Override
    public List<BasePlatePartsDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return basePlatePartsDetMapper.findList(map);
    }
}
