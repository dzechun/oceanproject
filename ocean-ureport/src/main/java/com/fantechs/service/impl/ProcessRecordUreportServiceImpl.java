package com.fantechs.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.dto.ProcessRecordUreportDto;
import com.fantechs.mapper.ProcessRecordUreportMapper;
import com.fantechs.service.ProcessRecordUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProcessRecordUreportServiceImpl implements ProcessRecordUreportService {

    @Resource
    private ProcessRecordUreportMapper processRecordUreportMapper;

    @Override
    public List<ProcessRecordUreportDto> findList(Map<String, Object> map) {
//        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
//        map.put("orgId", sysUser.getOrganizationId());

        if(StringUtils.isEmpty(map.get("finishDate"))){
            map.put("finishDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        }
//        return processRecordUreportMapper.findList(map);
        return new ArrayList<>();
    }
}
