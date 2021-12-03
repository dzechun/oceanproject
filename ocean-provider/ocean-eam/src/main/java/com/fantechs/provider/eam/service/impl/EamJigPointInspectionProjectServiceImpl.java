package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionProjectDto;
import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionProject;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionProjectItem;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigPointInspectionProject;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigPointInspectionProjectMapper;
import com.fantechs.provider.eam.mapper.EamJigPointInspectionProjectItemMapper;
import com.fantechs.provider.eam.mapper.EamJigPointInspectionProjectMapper;
import com.fantechs.provider.eam.service.EamJigPointInspectionProjectService;
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
 * Created by leifengzhi on 2021/08/16.
 */
@Service
public class EamJigPointInspectionProjectServiceImpl extends BaseService<EamJigPointInspectionProject> implements EamJigPointInspectionProjectService {

    @Resource
    private EamJigPointInspectionProjectMapper eamJigPointInspectionProjectMapper;
    @Resource
    private EamJigPointInspectionProjectItemMapper eamJigPointInspectionProjectItemMapper;
    @Resource
    private EamHtJigPointInspectionProjectMapper eamHtJigPointInspectionProjectMapper;

    @Override
    public List<EamJigPointInspectionProjectDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamJigPointInspectionProjectMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigPointInspectionProject record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(record,user);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        eamJigPointInspectionProjectMapper.insertUseGeneratedKeys(record);

        //点检项目事项
        List<EamJigPointInspectionProjectItemDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigPointInspectionProjectItem eamJigPointInspectionProjectItem : list){
                eamJigPointInspectionProjectItem.setJigPointInspectionProjectId(record.getJigPointInspectionProjectId());
                eamJigPointInspectionProjectItem.setCreateUserId(user.getUserId());
                eamJigPointInspectionProjectItem.setCreateTime(new Date());
                eamJigPointInspectionProjectItem.setModifiedUserId(user.getUserId());
                eamJigPointInspectionProjectItem.setModifiedTime(new Date());
                eamJigPointInspectionProjectItem.setStatus(StringUtils.isEmpty(eamJigPointInspectionProjectItem.getStatus())?1: eamJigPointInspectionProjectItem.getStatus());
                eamJigPointInspectionProjectItem.setOrgId(user.getOrganizationId());
            }
            eamJigPointInspectionProjectItemMapper.insertList(list);
        }

        EamHtJigPointInspectionProject eamHtJigPointInspectionProject = new EamHtJigPointInspectionProject();
        BeanUtils.copyProperties(record,eamHtJigPointInspectionProject);
        int i = eamHtJigPointInspectionProjectMapper.insertSelective(eamHtJigPointInspectionProject);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigPointInspectionProject entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(entity,user);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigPointInspectionProjectMapper.updateByPrimaryKeySelective(entity);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<EamJigPointInspectionProjectItemDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)) {
            for (EamJigPointInspectionProjectItemDto eamJigPointInspectionProjectItemDto : list) {
                if (StringUtils.isNotEmpty(eamJigPointInspectionProjectItemDto.getJigPointInspectionProjectItemId())) {
                    eamJigPointInspectionProjectItemMapper.updateByPrimaryKey(eamJigPointInspectionProjectItemDto);
                    idList.add(eamJigPointInspectionProjectItemDto.getJigPointInspectionProjectItemId());
                }
            }
        }

        //删除原点检项目事项
        Example example1 = new Example(EamJigPointInspectionProjectItem.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigPointInspectionProjectId", entity.getJigPointInspectionProjectId());
        if (idList.size() > 0) {
            criteria1.andNotIn("jigPointInspectionProjectItemId", idList);
        }
        eamJigPointInspectionProjectItemMapper.deleteByExample(example1);

        //点检项目事项
        if(StringUtils.isNotEmpty(list)){
            for (EamJigPointInspectionProjectItem eamJigPointInspectionProjectItem : list){
                if (idList.contains(eamJigPointInspectionProjectItem.getJigPointInspectionProjectItemId())) {
                    continue;
                }
                eamJigPointInspectionProjectItem.setJigPointInspectionProjectId(entity.getJigPointInspectionProjectId());
                eamJigPointInspectionProjectItem.setCreateUserId(user.getUserId());
                eamJigPointInspectionProjectItem.setCreateTime(new Date());
                eamJigPointInspectionProjectItem.setModifiedUserId(user.getUserId());
                eamJigPointInspectionProjectItem.setModifiedTime(new Date());
                eamJigPointInspectionProjectItem.setStatus(StringUtils.isEmpty(eamJigPointInspectionProjectItem.getStatus())?1: eamJigPointInspectionProjectItem.getStatus());
                eamJigPointInspectionProjectItem.setOrgId(user.getOrganizationId());
                eamJigPointInspectionProjectItemMapper.insert(eamJigPointInspectionProjectItem);
            }
        }

        EamHtJigPointInspectionProject eamHtJigPointInspectionProject = new EamHtJigPointInspectionProject();
        BeanUtils.copyProperties(entity,eamHtJigPointInspectionProject);
        int i = eamHtJigPointInspectionProjectMapper.insertSelective(eamHtJigPointInspectionProject);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtJigPointInspectionProject> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigPointInspectionProject eamJigPointInspectionProject = eamJigPointInspectionProjectMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigPointInspectionProject)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigPointInspectionProject eamHtJigPointInspectionProject = new EamHtJigPointInspectionProject();
            BeanUtils.copyProperties(eamJigPointInspectionProject,eamHtJigPointInspectionProject);
            htList.add(eamHtJigPointInspectionProject);

            //删除点检项目事项
            Example example1 = new Example(EamJigPointInspectionProjectItem.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigPointInspectionProjectId", id);
            eamJigPointInspectionProjectItemMapper.deleteByExample(example1);
        }

        eamHtJigPointInspectionProjectMapper.insertList(htList);

        return eamJigPointInspectionProjectMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(EamJigPointInspectionProject eamJigPointInspectionProject,SysUser user){

        //判断编码是否重复
        Example example = new Example(EamJigPointInspectionProject.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("jigPointInspectionProjectCode",eamJigPointInspectionProject.getJigPointInspectionProjectCode())
                .andEqualTo("orgId",user.getOrganizationId());
        if (StringUtils.isNotEmpty(eamJigPointInspectionProject.getJigPointInspectionProjectId())){
            criteria1.andNotEqualTo("jigPointInspectionProjectId",eamJigPointInspectionProject.getJigPointInspectionProjectId());
        }
        EamJigPointInspectionProject jigPointInspectionProject1 = eamJigPointInspectionProjectMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigPointInspectionProject1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andEqualTo("jigCategoryId",eamJigPointInspectionProject.getJigCategoryId());
        if (StringUtils.isNotEmpty(eamJigPointInspectionProject.getJigPointInspectionProjectId())){
            criteria2.andNotEqualTo("jigPointInspectionProjectId",eamJigPointInspectionProject.getJigPointInspectionProjectId());
        }
        EamJigPointInspectionProject jigPointInspectionProject2 = eamJigPointInspectionProjectMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigPointInspectionProject2)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"已存在绑定该治具类别的点检项目");
        }
    }
}
