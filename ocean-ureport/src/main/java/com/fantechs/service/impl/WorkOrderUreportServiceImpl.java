package com.fantechs.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.dto.WorkOrderUreportDto;
import com.fantechs.mapper.WorkOrderUreportMapper;
import com.fantechs.service.WorkOrderUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class WorkOrderUreportServiceImpl implements WorkOrderUreportService {

    @Resource
    private WorkOrderUreportMapper workOrderUreportMapper;

    @Override
    public List<WorkOrderUreportDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", sysUser.getOrganizationId());
        return workOrderUreportMapper.findList(map);
    }
}
