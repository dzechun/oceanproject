package com.fantechs.provider.guest.wanbao.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSampleBeforeRecheck;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.wanbao.mapper.QmsInspectionOrderDetSampleBeforeRecheckMapper;
import com.fantechs.provider.guest.wanbao.service.QmsInspectionOrderDetSampleBeforeRecheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/04/01.
 */
@Service
public class QmsInspectionOrderDetSampleBeforeRecheckServiceImpl extends BaseService<QmsInspectionOrderDetSampleBeforeRecheck> implements QmsInspectionOrderDetSampleBeforeRecheckService {

    @Resource
    private QmsInspectionOrderDetSampleBeforeRecheckMapper qmsInspectionOrderDetSampleBeforeRecheckMapper;

    @Override
    public List<QmsInspectionOrderDetSampleBeforeRecheck> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsInspectionOrderDetSampleBeforeRecheckMapper.findList(map);
    }

}
