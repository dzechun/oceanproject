package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.mapper.WmsInHtInPlanOrderDetMapper;
import com.fantechs.provider.wms.in.service.WmsInHtInPlanOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class WmsInHtInPlanOrderDetServiceImpl extends BaseService<WmsInHtInPlanOrderDet> implements WmsInHtInPlanOrderDetService {

    @Resource
    private WmsInHtInPlanOrderDetMapper wmsInHtInPlanOrderDetMapper;

    @Override
    public List<WmsInInPlanOrderDetDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
    //    return wmsInHtInPlanOrderDetMapper.findList(map);
        return null;
    }
}
