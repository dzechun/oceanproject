package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvProductionInLogDetDto;
import com.fantechs.common.base.general.dto.callagv.CallAgvProductionInLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvProductionInLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.mapper.CallAgvProductionInLogMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvProductionInLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CallAgvProductionInLogServiceImpl extends BaseService<CallAgvProductionInLog> implements CallAgvProductionInLogService {

    @Resource
    private CallAgvProductionInLogMapper callAgvProductionInLogMapper;

    @Override
    public List<CallAgvProductionInLogDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        List<CallAgvProductionInLogDto> callAgvProductionInLogDtoList = callAgvProductionInLogMapper.findList(map);

        return callAgvProductionInLogDtoList;
    }

    @Override
    public List<CallAgvProductionInLogDetDto> findDetList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return callAgvProductionInLogMapper.findDetList(map);
    }

    @Override
    public Map<String, Object> export(Map<String, Object> map) {
        List<CallAgvProductionInLogDto> callAgvProductionInLogDtoList = findList(map);
        List<CallAgvProductionInLogDetDto> callAgvProductionInLogDetDtoList = findDetList(map);
        Map<String, Object> resultMap = new LinkedHashMap<>();

        resultMap.put("生产出入库信息", callAgvProductionInLogDtoList);
        resultMap.put("生产出入库明细信息", callAgvProductionInLogDetDtoList);
        return resultMap;
    }
}
