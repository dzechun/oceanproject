package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetSampleDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDetSample;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.qms.mapper.QmsHtIncomingInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderDetSampleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */
@Service
public class QmsIncomingInspectionOrderDetSampleServiceImpl extends BaseService<QmsIncomingInspectionOrderDetSample> implements QmsIncomingInspectionOrderDetSampleService {

    @Resource
    private QmsIncomingInspectionOrderDetSampleMapper qmsIncomingInspectionOrderDetSampleMapper;

    @Resource
    private QmsHtIncomingInspectionOrderDetSampleMapper qmsHtIncomingInspectionOrderDetSampleMapper;

    @Override
    public List<QmsHtIncomingInspectionOrderDetSample> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsHtIncomingInspectionOrderDetSampleMapper.findHtList(map);
    }

    @Override
    public List<QmsIncomingInspectionOrderDetSampleDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsIncomingInspectionOrderDetSampleMapper.findList(map);
    }

}
