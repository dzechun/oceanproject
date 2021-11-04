package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.eng.EngMaterialMaintainLogDto;
import com.fantechs.common.base.general.entity.eng.EngMaterialMaintainLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.mapper.EngMaterialMaintainLogMapper;
import com.fantechs.provider.guest.eng.service.EngMaterialMaintainLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/04.
 */
@Service
public class EngMaterialMaintainLogServiceImpl extends BaseService<EngMaterialMaintainLog> implements EngMaterialMaintainLogService {

    @Resource
    private EngMaterialMaintainLogMapper engMaterialMaintainLogMapper;

    @Override
    public List<EngMaterialMaintainLogDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",user.getOrganizationId());
        }
        return engMaterialMaintainLogMapper.findList(map);
    }

    @Override
    public int save(EngMaterialMaintainLog record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setStatus((byte)1);
        record.setOrgId(user.getOrganizationId());
        record.setOperatorUserId(user.getUserId());
        return engMaterialMaintainLogMapper.insertSelective(record);
    }

    @Override
    public int update(EngMaterialMaintainLog entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        return engMaterialMaintainLogMapper.updateByPrimaryKeySelective(entity);
    }
}
