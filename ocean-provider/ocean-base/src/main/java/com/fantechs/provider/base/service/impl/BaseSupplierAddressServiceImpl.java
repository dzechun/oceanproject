package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseSupplierAddress;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseSupplierAddressMapper;
import com.fantechs.provider.base.service.BaseSupplierAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/05.
 */
@Service
public class BaseSupplierAddressServiceImpl extends BaseService<BaseSupplierAddress> implements BaseSupplierAddressService {

    @Resource
    private BaseSupplierAddressMapper baseSupplierAddressMapper;


    @Override
    public List<BaseSupplierAddress> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseSupplierAddressMapper.findList(map);
    }
}
