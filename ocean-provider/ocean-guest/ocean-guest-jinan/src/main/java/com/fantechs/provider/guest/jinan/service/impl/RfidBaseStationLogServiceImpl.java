package com.fantechs.provider.guest.jinan.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.jinan.RfidBaseStationLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.mapper.RfidBaseStationLogMapper;
import com.fantechs.provider.guest.jinan.service.RfidBaseStationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class RfidBaseStationLogServiceImpl extends BaseService<RfidBaseStationLog> implements RfidBaseStationLogService {

    @Resource
    private RfidBaseStationLogMapper rfidBaseStationLogMapper;

    @Override
    public List<RfidBaseStationLog> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return rfidBaseStationLogMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(RfidBaseStationLog record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        return rfidBaseStationLogMapper.insertSelective(record);
    }
}
