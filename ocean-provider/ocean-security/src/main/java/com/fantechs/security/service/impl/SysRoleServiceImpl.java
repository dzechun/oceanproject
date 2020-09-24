package com.fantechs.security.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysRoleExcelDTO;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.SysUserRole;
import com.fantechs.common.base.entity.security.history.SysHtRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysHtRoleMapper;
import com.fantechs.security.mapper.SysRoleMapper;
import com.fantechs.security.mapper.SysUserRoleMapper;
import com.fantechs.security.service.SysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends BaseService<SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysHtRoleMapper sysHtRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public List<SysRole> selectRoles(SearchSysRole searchSysRole) {
        return sysRoleMapper.selectRoles(searchSysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysRole sysRole) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException( ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleCode",sysRole.getRoleCode());
        List<SysRole> sysRoles = sysRoleMapper.selectByExample(example);
        if(null!=sysRoles&&sysRoles.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        sysRole.setCreateUserId(currentUser.getUserId());
        sysRole.setCreateTime(new Date());
        sysRoleMapper.insertUseGeneratedKeys(sysRole);

        //新增角色历史信息
        SysHtRole sysHtRole=new SysHtRole();
        BeanUtils.copyProperties(sysRole,sysHtRole);
        int i = sysHtRoleMapper.insertSelective(sysHtRole);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SysRole sysRole) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleCode",sysRole.getRoleCode());
        SysRole role = sysRoleMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(role)&&!role.getRoleId().equals(sysRole.getRoleId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        sysRole.setModifiedUserId(currentUser.getUserId());
        sysRole.setModifiedTime(new Date());
        int i= sysRoleMapper.updateByPrimaryKeySelective(sysRole);

        //新增角色历史信息
        SysHtRole sysHtRole=new SysHtRole();
        BeanUtils.copyProperties(sysRole,sysHtRole);
        sysHtRole.setModifiedUserId(currentUser.getUserId());
        sysHtRole.setModifiedTime(new Date());
        sysHtRoleMapper.insertSelective(sysHtRole);
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<Long> roleIds) {
        int i=0;
        List<SysHtRole> list=new ArrayList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        for (Long roleId : roleIds) {
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
            if(StringUtils.isEmpty(sysRole)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                //return ErrorCodeEnum.OPT20012003.getCode();
            }
            //新增角色历史信息
            SysHtRole sysHtRole=new SysHtRole();
            BeanUtils.copyProperties(sysRole,sysHtRole);
            sysHtRole.setModifiedUserId(currentUser.getUserId());
            sysHtRole.setModifiedTime(new Date());
            list.add(sysHtRole);

            sysRoleMapper.deleteByPrimaryKey(roleId);
        }

        i=sysHtRoleMapper.insertList(list);
        return i;
    }

    @Override
    public int addUser(Long roleId, List<Long> userIds) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SysUserRole> list=new ArrayList<>();
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId",roleId);
        int i = sysUserRoleMapper.deleteByExample(example);

        if(StringUtils.isNotEmpty(userIds)){
            for (Long userId : userIds) {
                SysUserRole sysUserRole=new SysUserRole();
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(roleId);

                list.add(sysUserRole);
            }
            sysUserRoleMapper.insertList(list);
        }
        return i;
    }
}
