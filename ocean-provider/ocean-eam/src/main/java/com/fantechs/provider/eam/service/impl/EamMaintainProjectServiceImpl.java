package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamMaintainProject;
import com.fantechs.common.base.general.entity.eam.history.EamHtMaintainProject;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtMaintainProjectMapper;
import com.fantechs.provider.eam.mapper.EamMaintainProjectMapper;
import com.fantechs.provider.eam.service.EamMaintainProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EamMaintainProjectServiceImpl extends BaseService<EamMaintainProject> implements EamMaintainProjectService {

    @Resource
    private EamMaintainProjectMapper eamMaintainProjectMapper;
    @Resource
    private EamHtMaintainProjectMapper eamHtMaintainProjectMapper;

    @Override
    public List<EamMaintainProjectDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamMaintainProjectMapper.findList(map);
    }

    @Override
    public List<EamHtMaintainProject> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamHtMaintainProjectMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamMaintainProject record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setMaintainProjectCode(CodeUtils.getId("BY-"));
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamMaintainProjectMapper.insertUseGeneratedKeys(record);

        EamHtMaintainProject eamHtMaintainProject = new EamHtMaintainProject();
        BeanUtils.copyProperties(record, eamHtMaintainProject);
        int i = eamHtMaintainProjectMapper.insert(eamHtMaintainProject);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamMaintainProject entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        EamHtMaintainProject eamHtMaintainProject = new EamHtMaintainProject();
        BeanUtils.copyProperties(entity, eamHtMaintainProject);
        eamHtMaintainProjectMapper.insert(eamHtMaintainProject);

        return eamMaintainProjectMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtMaintainProject> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamMaintainProject eamMaintainProject = eamMaintainProjectMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamMaintainProject)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtMaintainProject eamHtMaintainProject = new EamHtMaintainProject();
            BeanUtils.copyProperties(eamMaintainProject, eamHtMaintainProject);
            list.add(eamHtMaintainProject);
        }

        eamHtMaintainProjectMapper.insertList(list);

        return eamMaintainProjectMapper.deleteByIds(ids);
    }
}
