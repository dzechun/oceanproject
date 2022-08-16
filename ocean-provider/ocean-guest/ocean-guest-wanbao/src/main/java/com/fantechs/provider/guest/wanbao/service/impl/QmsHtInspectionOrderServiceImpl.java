package com.fantechs.provider.guest.wanbao.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.wanbao.history.QmsHtInspectionOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.wanbao.mapper.QmsHtInspectionOrderMapper;
import com.fantechs.provider.guest.wanbao.service.QmsHtInspectionOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */
@Service
public class QmsHtInspectionOrderServiceImpl extends BaseService<QmsHtInspectionOrder> implements QmsHtInspectionOrderService {

    @Resource
    private QmsHtInspectionOrderMapper qmsHtInspectionOrderMapper;

    @Override
    public List<QmsHtInspectionOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsHtInspectionOrderMapper.findHtList(map);
    }
}
