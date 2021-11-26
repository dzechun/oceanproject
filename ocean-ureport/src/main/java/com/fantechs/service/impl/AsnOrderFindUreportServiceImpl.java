package com.fantechs.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.ureport.BaseSupplierInfo;
import com.fantechs.common.base.general.entity.ureport.SrmAsnOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.mapper.AsnOrderFildUreportMapper;
import com.fantechs.mapper.SupplierUreportMapper;
import com.fantechs.service.AsnOrderFindUreportService;
import com.fantechs.service.SupplierUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AsnOrderFindUreportServiceImpl extends BaseService<SrmAsnOrder> implements AsnOrderFindUreportService {

    @Resource
    private AsnOrderFildUreportMapper asnOrderFildUreportMapper;

    @Override
    public List<SrmAsnOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        map.put("supplierId",user.getSupplierId());
        List<SrmAsnOrder> list = asnOrderFildUreportMapper.findList(map);
        return list;
    }

}
