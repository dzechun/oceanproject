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
import com.fantechs.provider.eam.mapper.EamEquPointInspectionProjectItemMapper;
import com.fantechs.provider.eam.mapper.EamEquPointInspectionProjectMapper;
import com.fantechs.provider.eam.mapper.EamHtEquPointInspectionProjectMapper;
import com.fantechs.provider.eam.service.EamEquPointInspectionProjectItemService;
import com.fantechs.provider.eam.service.EamEquPointInspectionProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
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
    private EamEquPointInspectionProjectItemMapper eamEquPointInspectionProjectItemMapper;

    @Resource
    private EamHtEquPointInspectionProjectMapper eamHtEquPointInspectionProjectMapper;

    @Resource
    private EamEquPointInspectionProjectItemService eamEquPointInspectionProjectItemService;

    @Override
    public List<EamEquPointInspectionProjectDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamEquPointInspectionProjectMapper.findList(map);
    }

    @Override
    public List<EamEquPointInspectionProjectDto> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquPointInspectionProjectMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquPointInspectionProject eamEquPointInspectionProject) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        codeIfRepeat(eamEquPointInspectionProject,user);

        // 新增点检项目
        eamEquPointInspectionProject.setCreateUserId(user.getUserId());
        eamEquPointInspectionProject.setCreateTime(new Date());
        eamEquPointInspectionProject.setModifiedUserId(user.getUserId());
        eamEquPointInspectionProject.setModifiedTime(new Date());
        eamEquPointInspectionProject.setOrgId(user.getOrganizationId());
        eamEquPointInspectionProject.setStatus(StringUtils.isEmpty(eamEquPointInspectionProject.getStatus())?1: eamEquPointInspectionProject.getStatus());
        eamEquPointInspectionProjectMapper.insertUseGeneratedKeys(eamEquPointInspectionProject);

        // 新增点检项目履历
        EamHtEquPointInspectionProject eamHtEquPointInspectionProject = new EamHtEquPointInspectionProject();
        BeanUtil.copyProperties(eamEquPointInspectionProject, eamHtEquPointInspectionProject);
        int i = eamHtEquPointInspectionProjectMapper.insertSelective(eamHtEquPointInspectionProject);

        //保养项目事项
        List<EamEquPointInspectionProjectItem> items = eamEquPointInspectionProject.getItems();
        if(StringUtils.isNotEmpty(items)){
            for (EamEquPointInspectionProjectItem eamEquPointInspectionProjectItem : items){
                eamEquPointInspectionProjectItem.setEquPointInspectionProjectId(eamEquPointInspectionProject.getEquPointInspectionProjectId());
                eamEquPointInspectionProjectItem.setCreateUserId(user.getUserId());
                eamEquPointInspectionProjectItem.setCreateTime(new Date());
                eamEquPointInspectionProjectItem.setModifiedUserId(user.getUserId());
                eamEquPointInspectionProjectItem.setModifiedTime(new Date());
                eamEquPointInspectionProjectItem.setStatus(StringUtils.isEmpty(eamEquPointInspectionProjectItem.getStatus())?1: eamEquPointInspectionProjectItem.getStatus());
                eamEquPointInspectionProjectItem.setOrgId(user.getOrganizationId());
            }
            eamEquPointInspectionProjectItemMapper.insertList(items);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquPointInspectionProject eamEquPointInspectionProject) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        codeIfRepeat(eamEquPointInspectionProject,user);

        // 修改点检项目
        eamEquPointInspectionProject.setModifiedUserId(user.getUserId());
        eamEquPointInspectionProject.setModifiedTime(new Date());
        eamEquPointInspectionProjectMapper.updateByPrimaryKeySelective(eamEquPointInspectionProject);

        // 新增点检项目履历
        EamHtEquPointInspectionProject eamHtEquPointInspectionProject = new EamHtEquPointInspectionProject();
        BeanUtil.copyProperties(eamEquPointInspectionProject, eamHtEquPointInspectionProject);
        int i = eamHtEquPointInspectionProjectMapper.insertSelective(eamHtEquPointInspectionProject);

        // 批量删除点检项目事项
        ArrayList<Long> idList = new ArrayList<>();
        List<EamEquPointInspectionProjectItem> items = eamEquPointInspectionProject.getItems();
        if(StringUtils.isNotEmpty(items)) {
            for (EamEquPointInspectionProjectItem eamEquPointInspectionProjectItem : items) {
                if (StringUtils.isNotEmpty(eamEquPointInspectionProjectItem.getEquPointInspectionProjectItemId())) {
                    eamEquPointInspectionProjectItemMapper.updateByPrimaryKey(eamEquPointInspectionProjectItem);
                    idList.add(eamEquPointInspectionProjectItem.getEquPointInspectionProjectItemId());
                }
            }
        }

        Example example = new Example(EamEquPointInspectionProjectItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equPointInspectionProjectId",eamEquPointInspectionProject.getEquPointInspectionProjectId());
        if (idList.size() > 0) {
            criteria.andNotIn("equPointInspectionProjectItemId", idList);
        }
        eamEquPointInspectionProjectItemMapper.deleteByExample(example);

        //点检项目事项
        if(StringUtils.isNotEmpty(items)){
            for (EamEquPointInspectionProjectItem eamEquPointInspectionProjectItem : items){
                if (idList.contains(eamEquPointInspectionProjectItem.getEquPointInspectionProjectItemId())) {
                    continue;
                }
                eamEquPointInspectionProjectItem.setEquPointInspectionProjectId(eamEquPointInspectionProject.getEquPointInspectionProjectId());
                eamEquPointInspectionProjectItem.setCreateUserId(user.getUserId());
                eamEquPointInspectionProjectItem.setCreateTime(new Date());
                eamEquPointInspectionProjectItem.setModifiedUserId(user.getUserId());
                eamEquPointInspectionProjectItem.setModifiedTime(new Date());
                eamEquPointInspectionProjectItem.setStatus(StringUtils.isEmpty(eamEquPointInspectionProjectItem.getStatus())?1: eamEquPointInspectionProjectItem.getStatus());
                eamEquPointInspectionProjectItem.setOrgId(user.getOrganizationId());
                eamEquPointInspectionProjectItemMapper.insert(eamEquPointInspectionProjectItem);
            }
        }

        return i;
    }

    private void codeIfRepeat(EamEquPointInspectionProject eamEquPointInspectionProject,SysUser user){
        //判断编码是否重复
        Example example = new Example(EamEquPointInspectionProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equPointInspectionProjectCode",eamEquPointInspectionProject.getEquPointInspectionProjectCode())
                .andEqualTo("orgId",user.getOrganizationId());
        if (StringUtils.isNotEmpty(eamEquPointInspectionProject.getEquPointInspectionProjectId())){
            criteria.andNotEqualTo("equPointInspectionProjectId",eamEquPointInspectionProject.getEquPointInspectionProjectId());
        }
        EamEquPointInspectionProject equPointInspectionProject = eamEquPointInspectionProjectMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(equPointInspectionProject)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("equipmentCategoryId",eamEquPointInspectionProject.getEquipmentCategoryId());
        if (StringUtils.isNotEmpty(eamEquPointInspectionProject.getEquPointInspectionProjectId())){
            criteria1.andNotEqualTo("equPointInspectionProjectId",eamEquPointInspectionProject.getEquPointInspectionProjectId());
        }
        EamEquPointInspectionProject equPointInspectionProject1 = eamEquPointInspectionProjectMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(equPointInspectionProject1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"已存在绑定该设备类别的点检项目");
        }
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
        List<String> idList = Arrays.asList(ids.split(","));
        map.put("projectIds", idList);
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
        return eamEquPointInspectionProjectMapper.deleteByIds(ids);
    }

}
