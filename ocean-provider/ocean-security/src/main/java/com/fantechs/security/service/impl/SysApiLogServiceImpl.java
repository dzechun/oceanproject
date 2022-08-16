package com.fantechs.security.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysApiLogDto;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysApiLogMapper;
import com.fantechs.security.service.SysApiLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */
@Service
public class SysApiLogServiceImpl extends BaseService<SysApiLog> implements SysApiLogService {

    @Resource
    private SysApiLogMapper sysApiLogMapper;

    @Override
    public List<SysApiLogDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser sysUser = currentUser();
            map.put("orgId", sysUser.getOrganizationId());
        }
        return sysApiLogMapper.findList(map);
    }

    @Override
    public int batchAdd(List<SysApiLog> logList) {
        for (SysApiLog log : logList){
            log.setCreateTime(new Date());
        }
        return sysApiLogMapper.insertList(logList);
    }

    @Override
    public int save(SysApiLog sysApiLog){
        //接口无登录用户
     //  SysUser sysUser = currentUser();
     //   sysApiLog.setOrgId(sysUser.getOrganizationId());
        sysApiLog.setCreateTime(new Date());
     //   sysApiLog.setCreateUserId(sysUser.getUserId());
        return sysApiLogMapper.insertSelective(sysApiLog);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
