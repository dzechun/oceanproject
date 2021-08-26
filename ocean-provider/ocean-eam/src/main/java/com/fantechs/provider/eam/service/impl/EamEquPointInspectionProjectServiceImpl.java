package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectDto;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProject;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProjectItem;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquPointInspectionProject;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquPointInspectionProjectMapper;
import com.fantechs.provider.eam.mapper.EamHtEquPointInspectionProjectMapper;
import com.fantechs.provider.eam.service.EamEquPointInspectionProjectItemService;
import com.fantechs.provider.eam.service.EamEquPointInspectionProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamEquPointInspectionProjectServiceImpl extends BaseService<EamEquPointInspectionProject> implements EamEquPointInspectionProjectService {

    @Resource
    private EamEquPointInspectionProjectMapper eamEquPointInspectionProjectMapper;

    @Resource
    private EamHtEquPointInspectionProjectMapper eamHtEquPointInspectionProjectMapper;

    @Resource
    private EamEquPointInspectionProjectItemService eamEquPointInspectionProjectItemService;

    @Override
    public List<EamEquPointInspectionProjectDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquPointInspectionProjectMapper.findList(map);
    }

    @Override
    public List<EamEquPointInspectionProjectDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquPointInspectionProjectMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquPointInspectionProject eamEquPointInspectionProject) {
        // 新增点检项目
        eamEquPointInspectionProjectMapper.insertUseGeneratedKeys(eamEquPointInspectionProject);

        // 新增点检项目履历
        EamHtEquPointInspectionProject eamHtEquPointInspectionProject = new EamHtEquPointInspectionProject();
        BeanUtil.copyProperties(eamEquPointInspectionProject, eamHtEquPointInspectionProject);
        int i = eamHtEquPointInspectionProjectMapper.insert(eamHtEquPointInspectionProject);

        if(!eamEquPointInspectionProject.getItems().isEmpty()){
            List<EamEquPointInspectionProjectItem> projectItems = eamEquPointInspectionProject.getItems()
                    .stream()
                    .map(item -> {
                        item.setEquPointInspectionProjectId(eamEquPointInspectionProject.getEquPointInspectionProjectId());
                        return item;
                    }).collect(Collectors.toList());
            // 批量新增点检项目事项及其履历
            eamEquPointInspectionProjectItemService.batchSave(projectItems);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquPointInspectionProject eamEquPointInspectionProject) {
        // 修改点检项目
        eamEquPointInspectionProjectMapper.updateByPrimaryKey(eamEquPointInspectionProject);

        // 新增点检项目履历
        EamHtEquPointInspectionProject eamHtEquPointInspectionProject = new EamHtEquPointInspectionProject();
        BeanUtil.copyProperties(eamEquPointInspectionProject, eamHtEquPointInspectionProject);
        int i = eamHtEquPointInspectionProjectMapper.insert(eamHtEquPointInspectionProject);

        if(!eamEquPointInspectionProject.getItems().isEmpty()){
            // 批量删除点检项目事项
            eamEquPointInspectionProjectItemService.batchDelete(eamEquPointInspectionProject.getItems());
            // 批量新增点检项目事项及其履历
            List<EamEquPointInspectionProjectItem> projectItems = eamEquPointInspectionProject.getItems()
                    .stream()
                    .map(item -> {
                        item.setEquPointInspectionProjectId(eamEquPointInspectionProject.getEquPointInspectionProjectId());
                        return item;
                    }).collect(Collectors.toList());
            eamEquPointInspectionProjectItemService.batchSave(projectItems);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateStatus(EamEquPointInspectionProject eamEquPointInspectionProject) {
        // 修改点检项目
        return eamEquPointInspectionProjectMapper.updateByPrimaryKeySelective(eamEquPointInspectionProject);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids){
        // 查询点检事项集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("projectIds", ids);
        List<EamEquPointInspectionProjectItemDto> list = eamEquPointInspectionProjectItemService.findList(map);
        if(!list.isEmpty()){
            List<EamEquPointInspectionProjectItem> projectItems = list.stream().map(item -> {
                EamEquPointInspectionProjectItem projectItem = new EamEquPointInspectionProjectItem();
                BeanUtil.copyProperties(item, projectItem);
                return projectItem;
            }).collect(Collectors.toList());
            // 批量删除点检项目事项
            eamEquPointInspectionProjectItemService.batchDelete(projectItems);
        }
        // 批量删除点检项目
        return this.batchDelete(ids);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
