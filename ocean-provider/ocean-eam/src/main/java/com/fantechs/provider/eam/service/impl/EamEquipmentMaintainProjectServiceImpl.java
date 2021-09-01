package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectItemDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProject;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProjectItem;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProjectItem;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaintainProject;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMaintainProjectItemMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentMaintainProjectMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMaintainProjectMapper;
import com.fantechs.provider.eam.service.EamEquipmentMaintainProjectItemService;
import com.fantechs.provider.eam.service.EamEquipmentMaintainProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
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
    private EamEquipmentMaintainProjectItemMapper eamEquipmentMaintainProjectItemMapper;

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
        SysUser user = getUser();

        codeIfRepeat(eamEquipmentMaintainProject);

        // 新增保养项目
        eamEquipmentMaintainProject.setCreateUserId(user.getUserId());
        eamEquipmentMaintainProject.setCreateTime(new Date());
        eamEquipmentMaintainProject.setModifiedUserId(user.getUserId());
        eamEquipmentMaintainProject.setModifiedTime(new Date());
        eamEquipmentMaintainProject.setOrgId(user.getOrganizationId());
        eamEquipmentMaintainProject.setStatus(StringUtils.isEmpty(eamEquipmentMaintainProject.getStatus())?1: eamEquipmentMaintainProject.getStatus());
        eamEquipmentMaintainProjectMapper.insertUseGeneratedKeys(eamEquipmentMaintainProject);

        // 新增保养项目履历
        EamHtEquipmentMaintainProject eamHtEquipmentMaintainProject = new EamHtEquipmentMaintainProject();
        BeanUtil.copyProperties(eamEquipmentMaintainProject, eamHtEquipmentMaintainProject);
        int i = eamHtEquipmentMaintainProjectMapper.insert(eamHtEquipmentMaintainProject);

        //保养项目事项
        List<EamEquipmentMaintainProjectItem> items = eamEquipmentMaintainProject.getItems();
        if(StringUtils.isNotEmpty(items)){
            for (EamEquipmentMaintainProjectItem eamEquipmentMaintainProjectItem : items){
                eamEquipmentMaintainProjectItem.setEquipmentMaintainProjectId(eamEquipmentMaintainProject.getEquipmentMaintainProjectId());
                eamEquipmentMaintainProjectItem.setCreateUserId(user.getUserId());
                eamEquipmentMaintainProjectItem.setCreateTime(new Date());
                eamEquipmentMaintainProjectItem.setModifiedUserId(user.getUserId());
                eamEquipmentMaintainProjectItem.setModifiedTime(new Date());
                eamEquipmentMaintainProjectItem.setStatus(StringUtils.isEmpty(eamEquipmentMaintainProjectItem.getStatus())?1: eamEquipmentMaintainProjectItem.getStatus());
                eamEquipmentMaintainProjectItem.setOrgId(user.getOrganizationId());
            }
            eamEquipmentMaintainProjectItemMapper.insertList(items);
        }

        return i;
    }

    @Override
    @Transactional
    public int update(EamEquipmentMaintainProject eamEquipmentMaintainProject) {
        SysUser user = getUser();

        codeIfRepeat(eamEquipmentMaintainProject);

        // 修改保养项目
        eamEquipmentMaintainProject.setModifiedUserId(user.getUserId());
        eamEquipmentMaintainProject.setModifiedTime(new Date());
        eamEquipmentMaintainProjectMapper.updateByPrimaryKeySelective(eamEquipmentMaintainProject);

        // 新增保养项目履历
        EamHtEquipmentMaintainProject eamHtEquipmentMaintainProject = new EamHtEquipmentMaintainProject();
        BeanUtil.copyProperties(eamEquipmentMaintainProject, eamHtEquipmentMaintainProject);
        int i = eamHtEquipmentMaintainProjectMapper.insert(eamHtEquipmentMaintainProject);

        // 批量删除保养项目事项
        ArrayList<Long> idList = new ArrayList<>();
        List<EamEquipmentMaintainProjectItem> items = eamEquipmentMaintainProject.getItems();
        if(StringUtils.isNotEmpty(items)) {
            for (EamEquipmentMaintainProjectItem eamEquipmentMaintainProjectItem : items) {
                if (StringUtils.isNotEmpty(eamEquipmentMaintainProjectItem.getEquipmentMaintainProjectItemId())) {
                    eamEquipmentMaintainProjectItemMapper.updateByPrimaryKey(eamEquipmentMaintainProjectItem);
                    idList.add(eamEquipmentMaintainProjectItem.getEquipmentMaintainProjectItemId());
                }
            }
        }

        Example example = new Example(EamEquipmentMaintainProjectItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentMaintainProjectId",eamEquipmentMaintainProject.getEquipmentMaintainProjectId());
        if (idList.size() > 0) {
            criteria.andNotIn("equipmentMaintainProjectItemId", idList);
        }
        eamEquipmentMaintainProjectItemMapper.deleteByExample(example);

        //保养项目事项
        if(StringUtils.isNotEmpty(items)){
            for (EamEquipmentMaintainProjectItem eamEquipmentMaintainProjectItem : items){
                if (idList.contains(eamEquipmentMaintainProjectItem.getEquipmentMaintainProjectItemId())) {
                    continue;
                }
                eamEquipmentMaintainProjectItem.setEquipmentMaintainProjectId(eamEquipmentMaintainProject.getEquipmentMaintainProjectId());
                eamEquipmentMaintainProjectItem.setCreateUserId(user.getUserId());
                eamEquipmentMaintainProjectItem.setCreateTime(new Date());
                eamEquipmentMaintainProjectItem.setModifiedUserId(user.getUserId());
                eamEquipmentMaintainProjectItem.setModifiedTime(new Date());
                eamEquipmentMaintainProjectItem.setStatus(StringUtils.isEmpty(eamEquipmentMaintainProjectItem.getStatus())?1: eamEquipmentMaintainProjectItem.getStatus());
                eamEquipmentMaintainProjectItem.setOrgId(user.getOrganizationId());
                eamEquipmentMaintainProjectItemMapper.insert(eamEquipmentMaintainProjectItem);
            }
        }

        return i;
    }

    private void codeIfRepeat(EamEquipmentMaintainProject eamEquipmentMaintainProject){
        //判断编码是否重复
        Example example = new Example(EamEquipmentMaintainProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentMaintainProjectCode",eamEquipmentMaintainProject.getEquipmentMaintainProjectCode());
        if (StringUtils.isNotEmpty(eamEquipmentMaintainProject.getEquipmentMaintainProjectId())){
            criteria.andNotEqualTo("equipmentMaintainProjectId",eamEquipmentMaintainProject.getEquipmentMaintainProjectId());
        }
        EamEquipmentMaintainProject equipmentMaintainProject = eamEquipmentMaintainProjectMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(equipmentMaintainProject)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("equipmentCategoryId",eamEquipmentMaintainProject.getEquipmentCategoryId());
        if (StringUtils.isNotEmpty(eamEquipmentMaintainProject.getEquipmentMaintainProjectId())){
            criteria1.andNotEqualTo("equipmentMaintainProjectId",eamEquipmentMaintainProject.getEquipmentMaintainProjectId());
        }
        EamEquipmentMaintainProject equipmentMaintainProject1 = eamEquipmentMaintainProjectMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(equipmentMaintainProject1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"已存在绑定该设备类别的保养项目");
        }
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
        List<String> idList = Arrays.asList(ids.split(","));
        map.put("projectIds", idList);
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
        return eamEquipmentMaintainProjectMapper.deleteByIds(ids);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
