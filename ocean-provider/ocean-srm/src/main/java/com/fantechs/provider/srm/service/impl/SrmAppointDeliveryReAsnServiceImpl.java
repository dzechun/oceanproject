package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.entity.srm.SrmAppointDeliveryReAsn;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmAppointDeliveryReAsnMapper;
import com.fantechs.provider.srm.service.SrmAppointDeliveryReAsnService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */
@Service
public class SrmAppointDeliveryReAsnServiceImpl extends BaseService<SrmAppointDeliveryReAsn> implements SrmAppointDeliveryReAsnService {

    @Resource
    private SrmAppointDeliveryReAsnMapper srmAppointDeliveryReAsnMapper;

    @Override
    public List<SrmAppointDeliveryReAsnDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        return srmAppointDeliveryReAsnMapper.findList(map);
    }

}
