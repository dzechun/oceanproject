package com.fantechs.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.general.entity.ureport.BaseSupplierInfo;
import com.fantechs.mapper.SupplierUreportMapper;
import com.fantechs.service.SupplierUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SupplierUreportServiceImpl extends BaseService<BaseSupplierInfo> implements SupplierUreportService {

    @Resource
    private SupplierUreportMapper supplierUreportMapper;

    @Override
    public List<BaseSupplierInfo> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        List<BaseSupplierInfo> list = supplierUreportMapper.findList(map);
        return list;
    }

}
