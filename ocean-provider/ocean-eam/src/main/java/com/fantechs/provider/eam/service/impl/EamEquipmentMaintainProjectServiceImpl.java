package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProject;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProjectItem;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaintainProject;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMaintainProjectMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMaintainProjectMapper;
import com.fantechs.provider.eam.service.EamEquipmentMaintainProjectItemService;
import com.fantechs.provider.eam.service.EamEquipmentMaintainProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentMaintainProjectServiceImpl extends BaseService<EamEquipmentMaintainProject> implements EamEquipmentMaintainProjectService {

    @Resource
    private EamEquipmentMaintainProjectMapper eamEquipmentMaintainProjectMapper;

    @Resource
    private EamHtEquipmentMaintainProjectMapper eamHtEquipmentMaintainProjectMapper;
    
    @Resource
    private EamEquipmentMaintainProjectItemService eamEquipmentMaintainProjectItemService;

    @Override
    public List<EamEquipmentMaintainProjectDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMaintainProjectMapper.findList(map);
    }

    @Override
    public List<EamEquipmentMaintainProjectDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMaintainProjectMapper.findList(map);
    }

    @Override
    @Transactional
    public int save(EamEquipmentMaintainProject eamEquipmentMaintainProject) {
        // 新增保养项目
        eamEquipmentMaintainProjectMapper.insertUseGeneratedKeys(eamEquipmentMaintainProject);

        // 新增保养项目履历
        EamHtEquipmentMaintainProject eamHtEquipmentMaintainProject = new EamHtEquipmentMaintainProject();
        BeanUtil.copyProperties(eamEquipmentMaintainProject, eamHtEquipmentMaintainProject);
        int i = eamHtEquipmentMaintainProjectMapper.insert(eamHtEquipmentMaintainProject);

        if(!eamEquipmentMaintainProject.getItems().isEmpty()){
            List<EamEquipmentMaintainProjectItem> maintainProjectItems = eamEquipmentMaintainProject.getItems()
                    .stream()
                    .map(item -> {
                        item.setEquipmentMaintainProjectId(eamEquipmentMaintainProject.getEquipmentMaintainProjectId());
                        return item;
                    }).collect(Collectors.toList());
            // 批量新增保养项目事项及其履历
            eamEquipmentMaintainProjectItemService.batchSave(maintainProjectItems);
        }
        return i;
    }

    @Override
    @Transactional
    public int update(EamEquipmentMaintainProject eamEquipmentMaintainProject) {
        // 修改保养项目
        eamEquipmentMaintainProjectMapper.updateByPrimaryKey(eamEquipmentMaintainProject);

        // 新增保养项目履历
        EamHtEquipmentMaintainProject eamHtEquipmentMaintainProject = new EamHtEquipmentMaintainProject();
        BeanUtil.copyProperties(eamEquipmentMaintainProject, eamHtEquipmentMaintainProject);
        int i = eamHtEquipmentMaintainProjectMapper.insert(eamHtEquipmentMaintainProject);

        if(!eamEquipmentMaintainProject.getItems().isEmpty()){
            // 批量删除保养项目事项
            eamEquipmentMaintainProjectItemService.batchDelete(eamEquipmentMaintainProject.getItems());
            // 批量新增保养项目事项及其履历
            List<EamEquipmentMaintainProjectItem> maintainProjectItems = eamEquipmentMaintainProject.getItems()
                    .stream()
                    .map(item -> {
                        item.setEquipmentMaintainProjectId(eamEquipmentMaintainProject.getEquipmentMaintainProjectId());
                        return item;
                    }).collect(Collectors.toList());
            eamEquipmentMaintainProjectItemService.batchSave(maintainProjectItems);
        }
        return i;
    }

    @Override
    public int updateStatus(EamEquipmentMaintainProject eamEquipmentMaintainProject) {
        // 修改保养项目状态
        return eamEquipmentMaintainProjectMapper.updateByPrimaryKeySelective(eamEquipmentMaintainProject);
    }

    @Override
    @Transactional
    public int batchDelete(String ids){
        // 查询保养事项集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("projectIds", ids);
        List<EamEquipmentMaintainProjectItemDto> list = eamEquipmentMaintainProjectItemService.findList(map);
        if(!list.isEmpty()){
            List<EamEquipmentMaintainProjectItem> projectItems = list.stream().map(item -> {
                EamEquipmentMaintainProjectItem projectItem = new EamEquipmentMaintainProjectItem();
                BeanUtil.copyProperties(item, projectItem);
                return projectItem;
            }).collect(Collectors.toList());
            // 批量删除保养项目事项
            eamEquipmentMaintainProjectItemService.batchDelete(projectItems);
        }
        // 批量删除保养项目
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
