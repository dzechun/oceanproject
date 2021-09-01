package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainProjectDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProjectItem;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProject;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigMaintainProjectMapper;
import com.fantechs.provider.eam.mapper.EamJigMaintainProjectItemMapper;
import com.fantechs.provider.eam.mapper.EamJigMaintainProjectMapper;
import com.fantechs.provider.eam.service.EamJigMaintainProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/12.
 */
@Service
public class EamJigMaintainProjectServiceImpl extends BaseService<EamJigMaintainProject> implements EamJigMaintainProjectService {

    @Resource
    private EamJigMaintainProjectMapper eamJigMaintainProjectMapper;
    @Resource
    private EamJigMaintainProjectItemMapper eamJigMaintainProjectItemMapper;
    @Resource
    private EamHtJigMaintainProjectMapper eamHtJigMaintainProjectMapper;

    @Override
    public List<EamJigMaintainProjectDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigMaintainProjectMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigMaintainProject record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        eamJigMaintainProjectMapper.insertUseGeneratedKeys(record);

        //保养项目事项
        List<EamJigMaintainProjectItemDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigMaintainProjectItem eamJigMaintainProjectItem : list){
                eamJigMaintainProjectItem.setJigMaintainProjectId(record.getJigMaintainProjectId());
                eamJigMaintainProjectItem.setCreateUserId(user.getUserId());
                eamJigMaintainProjectItem.setCreateTime(new Date());
                eamJigMaintainProjectItem.setModifiedUserId(user.getUserId());
                eamJigMaintainProjectItem.setModifiedTime(new Date());
                eamJigMaintainProjectItem.setStatus(StringUtils.isEmpty(eamJigMaintainProjectItem.getStatus())?1: eamJigMaintainProjectItem.getStatus());
                eamJigMaintainProjectItem.setOrgId(user.getOrganizationId());
            }
            eamJigMaintainProjectItemMapper.insertList(list);
        }

        EamHtJigMaintainProject eamHtJigMaintainProject = new EamHtJigMaintainProject();
        BeanUtils.copyProperties(record,eamHtJigMaintainProject);
        int i = eamHtJigMaintainProjectMapper.insert(eamHtJigMaintainProject);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigMaintainProject entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigMaintainProjectMapper.updateByPrimaryKeySelective(entity);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<EamJigMaintainProjectItemDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)) {
            for (EamJigMaintainProjectItemDto eamJigMaintainProjectItemDto : list) {
                if (StringUtils.isNotEmpty(eamJigMaintainProjectItemDto.getJigMaintainProjectItemId())) {
                    eamJigMaintainProjectItemMapper.updateByPrimaryKey(eamJigMaintainProjectItemDto);
                    idList.add(eamJigMaintainProjectItemDto.getJigMaintainProjectItemId());
                }
            }
        }

        //删除原保养项目事项
        Example example1 = new Example(EamJigMaintainProjectItem.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigMaintainProjectId", entity.getJigMaintainProjectId());
        if (idList.size() > 0) {
            criteria1.andNotIn("jigMaintainProjectItemId", idList);
        }
        eamJigMaintainProjectItemMapper.deleteByExample(example1);

        //保养项目事项
        if(StringUtils.isNotEmpty(list)){
            for (EamJigMaintainProjectItem eamJigMaintainProjectItem : list){
                if (idList.contains(eamJigMaintainProjectItem.getJigMaintainProjectItemId())) {
                    continue;
                }
                eamJigMaintainProjectItem.setJigMaintainProjectId(entity.getJigMaintainProjectId());
                eamJigMaintainProjectItem.setCreateUserId(user.getUserId());
                eamJigMaintainProjectItem.setCreateTime(new Date());
                eamJigMaintainProjectItem.setModifiedUserId(user.getUserId());
                eamJigMaintainProjectItem.setModifiedTime(new Date());
                eamJigMaintainProjectItem.setStatus(StringUtils.isEmpty(eamJigMaintainProjectItem.getStatus())?1: eamJigMaintainProjectItem.getStatus());
                eamJigMaintainProjectItem.setOrgId(user.getOrganizationId());
                eamJigMaintainProjectItemMapper.insert(eamJigMaintainProjectItem);
            }
        }

        EamHtJigMaintainProject eamHtJigMaintainProject = new EamHtJigMaintainProject();
        BeanUtils.copyProperties(entity,eamHtJigMaintainProject);
        int i = eamHtJigMaintainProjectMapper.insert(eamHtJigMaintainProject);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigMaintainProject> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigMaintainProject eamJigMaintainProject = eamJigMaintainProjectMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigMaintainProject)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigMaintainProject eamHtJigMaintainProject = new EamHtJigMaintainProject();
            BeanUtils.copyProperties(eamJigMaintainProject,eamHtJigMaintainProject);
            htList.add(eamHtJigMaintainProject);

            //删除保养项目事项
            Example example1 = new Example(EamJigMaintainProjectItem.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigMaintainProjectId", id);
            eamJigMaintainProjectItemMapper.deleteByExample(example1);
        }

        eamHtJigMaintainProjectMapper.insertList(htList);

        return eamJigMaintainProjectMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(EamJigMaintainProject eamJigMaintainProject){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(EamJigMaintainProject.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("jigMaintainProjectCode",eamJigMaintainProject.getJigMaintainProjectCode());
        if (StringUtils.isNotEmpty(eamJigMaintainProject.getJigMaintainProjectId())){
            criteria1.andNotEqualTo("jigMaintainProjectId",eamJigMaintainProject.getJigMaintainProjectId());
        }
        EamJigMaintainProject jigMaintainProject1 = eamJigMaintainProjectMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigMaintainProject1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andEqualTo("jigCategoryId",eamJigMaintainProject.getJigCategoryId());
        if (StringUtils.isNotEmpty(eamJigMaintainProject.getJigMaintainProjectId())){
            criteria2.andNotEqualTo("jigMaintainProjectId",eamJigMaintainProject.getJigMaintainProjectId());
        }
        EamJigMaintainProject jigMaintainProject2 = eamJigMaintainProjectMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigMaintainProject2)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"已存在绑定该治具类别的保养项目");
        }
    }
}
