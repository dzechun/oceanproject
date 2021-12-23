package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutHtPlanDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutPlanDeliveryOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@Service
public class WmsOutPlanDeliveryOrderDetServiceImpl extends BaseService<WmsOutPlanDeliveryOrderDet> implements WmsOutPlanDeliveryOrderDetService {

    @Resource
    private WmsOutPlanDeliveryOrderDetMapper wmsOutPlanDeliveryOrderDetMapper;
    @Resource
    private WmsOutHtPlanDeliveryOrderDetMapper wmsOutHtPlanDeliveryOrderDetMapper;

    @Override
    public List<WmsOutPlanDeliveryOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutPlanDeliveryOrderDetMapper.findList(map);
    }

    @Override
    public List<WmsOutHtPlanDeliveryOrderDet> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutHtPlanDeliveryOrderDetMapper.findHtList(map);
    }
}
