package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.mapper.WmsInInPlanOrderDetMapper;
import com.fantechs.provider.wms.in.service.WmsInInPlanOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class WmsInInPlanOrderDetServiceImpl extends BaseService<WmsInInPlanOrderDet> implements WmsInInPlanOrderDetService {

    @Resource
    private WmsInInPlanOrderDetMapper wmsInInPlanOrderDetMapper;

    @Override
    public List<WmsInInPlanOrderDetDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        return wmsInInPlanOrderDetMapper.findList(map);
    }


    @Override
    public List<WmsInInPlanOrderDetDto> findListByIds(String ids) {

        Map map = new HashMap();
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        String[] str = ids.split(",");
        List<String> list = Arrays.asList(str);
        map.put("inPlanOrderIds", list);
        return  wmsInInPlanOrderDetMapper.findList(map);
    }


}
