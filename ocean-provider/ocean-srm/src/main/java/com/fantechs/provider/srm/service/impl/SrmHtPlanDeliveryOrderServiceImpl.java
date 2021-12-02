package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPlanDeliveryOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmHtPlanDeliveryOrderMapper;
import com.fantechs.provider.srm.service.SrmHtPlanDeliveryOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class SrmHtPlanDeliveryOrderServiceImpl extends BaseService<SrmHtPlanDeliveryOrder> implements SrmHtPlanDeliveryOrderService {

    @Resource
    private SrmHtPlanDeliveryOrderMapper srmHtPlanDeliveryOrderMapper;

    @Override
    public List<SrmHtPlanDeliveryOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(map.get("orgId"))) {
            map.put("orgId",user.getOrganizationId());
        }


        if (StringUtils.isNotEmpty(user.getSupplierId())) {
            map.put("supplierId", user.getSupplierId());
        }
        return srmHtPlanDeliveryOrderMapper.findList(map);
    }

}
