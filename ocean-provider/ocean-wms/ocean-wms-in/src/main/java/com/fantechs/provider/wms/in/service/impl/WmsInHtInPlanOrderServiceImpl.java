package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.in.WmsInHtInPlanOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.mapper.WmsInHtInPlanOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInHtInPlanOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class WmsInHtInPlanOrderServiceImpl extends BaseService<WmsInHtInPlanOrder> implements WmsInHtInPlanOrderService {

    @Resource
    private WmsInHtInPlanOrderMapper wmsInHtInPlanOrderMapper;

    @Override
    public List<WmsInHtInPlanOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        return wmsInHtInPlanOrderMapper.findList(map);
    }

}
