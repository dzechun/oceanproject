package com.fantechs.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.ureport.BaseSupplierInfo;
import com.fantechs.common.base.general.entity.ureport.OmPurchaseOrderUreport;
import com.fantechs.common.base.general.entity.ureport.SrmAsnOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.mapper.AsnOrderFildUreportMapper;
import com.fantechs.mapper.OmPurchaseOrderUreportMapper;
import com.fantechs.service.AsnOrderFindUreportService;
import com.fantechs.service.OmPurchaseOrderUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class OmPurchaseOrderUreportServiceImpl extends BaseService<OmPurchaseOrderUreport> implements OmPurchaseOrderUreportService {
    @Resource
    private OmPurchaseOrderUreportMapper omPurchaseOrderUreportMapper;

    @Override
    public List<OmPurchaseOrderUreport> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        map.put("supplierId",user.getSupplierId());
        List<OmPurchaseOrderUreport> list = omPurchaseOrderUreportMapper.findList(map);
        return list;
    }
}
