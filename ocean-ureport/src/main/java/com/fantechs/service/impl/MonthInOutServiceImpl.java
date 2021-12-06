package com.fantechs.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.dto.MonthInDto;
import com.fantechs.dto.MonthOutDto;
import com.fantechs.dto.ShipmentDetDto;
import com.fantechs.mapper.MonthInOutMapper;
import com.fantechs.service.MonthInOutService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */
@Service
public class MonthInOutServiceImpl implements MonthInOutService {

    @Resource
    private MonthInOutMapper monthInOutMapper;

    @Override
    public List<MonthInDto> findInList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return monthInOutMapper.findInList(map);
    }

    @Override
    public List<String> findInListBarCode(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return monthInOutMapper.findInListBarCode(map);
    }

    @Override
    public List<MonthOutDto> findOutList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return monthInOutMapper.findOutList(map);
    }

    @Override
    public List<String> findOutListBarCode(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return monthInOutMapper.findOutListBarCode(map);
    }

    @Override
    public List<ShipmentDetDto> findShipmentDet(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return monthInOutMapper.findShipmentDet(map);
    }
}
