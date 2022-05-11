package com.fantechs.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.dto.ProcessRecordUreportDto;
import com.fantechs.mapper.ProcessRecordUreportMapper;
import com.fantechs.service.ProcessRecordUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ProcessRecordUreportServiceImpl implements ProcessRecordUreportService {

    @Resource
    private ProcessRecordUreportMapper processRecordUreportMapper;

    @Override
    public List<ProcessRecordUreportDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", sysUser.getOrganizationId());
        return processRecordUreportMapper.findList(map);
    }
}
