package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.in.mapper.WmsInReceivingOrderDetMapper;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@Service
public class WmsInReceivingOrderDetServiceImpl extends BaseService<WmsInReceivingOrderDet> implements WmsInReceivingOrderDetService {

    @Resource
    private WmsInReceivingOrderDetMapper wmsInReceivingOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<WmsInReceivingOrderDetDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        return wmsInReceivingOrderDetMapper.findList(map);
    }
}
