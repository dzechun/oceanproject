package com.fantechs.provider.guest.jinan.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.jinan.history.RfidHtArea;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.jinan.mapper.RfidHtAreaMapper;
import com.fantechs.provider.guest.jinan.service.RfidHtAreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class RfidHtAreaServiceImpl extends BaseService<RfidHtArea> implements RfidHtAreaService {

    @Resource
    private RfidHtAreaMapper rfidHtAreaMapper;

    @Override
    public List<RfidHtArea> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return rfidHtAreaMapper.findHtList(map);
    }

}
