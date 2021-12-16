package com.fantechs.provider.om.service.ht.impl;


import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.om.mapper.ht.OmHtSalesOrderDetMapper;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class OmHtSalesOrderDetServiceImpl extends BaseService<OmHtSalesOrderDet> implements OmHtSalesOrderDetService {

    @Resource
    private OmHtSalesOrderDetMapper omHtSalesOrderDetMapper;

    @Override
    public List<OmHtSalesOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return omHtSalesOrderDetMapper.findList(map);
    }
}
