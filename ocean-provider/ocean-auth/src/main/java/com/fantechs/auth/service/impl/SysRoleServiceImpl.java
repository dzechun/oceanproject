package com.fantechs.auth.service.impl;

import com.fantechs.auth.mapper.SysHtRoleMapper;
import com.fantechs.auth.mapper.SysRoleMapper;
import com.fantechs.auth.mapper.SysUserRoleMapper;
import com.fantechs.auth.service.SysRoleService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.SysUserRole;
import com.fantechs.common.base.entity.security.history.SysHtRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysRoleServiceImpl extends BaseService<SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysHtRoleMapper sysHtRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public List<SysRoleDto> selectRoles(SearchSysRole searchSysRole) {
        return sysRoleMapper.selectRoles(searchSysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysRole sysRole) {
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
        sysRole.setModifiedTime(new Date());
        sysRole.setModifiedUserId(currentUser.getUserId());
        sysRole.setStatus((byte)1);
        sysRole.setIsDelete((byte)1);
        sysRoleMapper.insertUseGeneratedKeys(sysRole);

        //新增角色历史信息
        SysHtRole sysHtRole=new SysHtRole();
        BeanUtils.copyProperties(sysRole,sysHtRole);
        int i = sysHtRoleMapper.insertSelective(sysHtRole);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysRole sysRole) {
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
        sysHtRoleMapper.insertSelective(sysHtRole);
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String roleIds) {
        int i=0;
        List<SysHtRole> list=new ArrayList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = roleIds.split(",");
        for (String roleId : idsArr) {
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
    @Transactional(rollbackFor = Exception.class)
    public int addUser(Long roleId, List<Long> userIds) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SysUserRole> list=new ArrayList<>();
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId",roleId);
        sysUserRoleMapper.deleteByExample(example);

        for (Long userId : userIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        if (StringUtils.isNotEmpty(list)){
            return sysUserRoleMapper.insertList(list);
        }
        return 1;
    }

    @Override
    public int addUserRole(Long userId, List<Long> roleIds) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<SysUserRole> list=new ArrayList<>();
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        sysUserRoleMapper.deleteByExample(example);

        for (Long roleId : roleIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        if (StringUtils.isNotEmpty(list)){
            return sysUserRoleMapper.insertList(list);
        }
        return 0;
    }

    @Override
    public List<SysRoleDto> findByUserName(SearchSysRole searchSysRole) {
        return sysRoleMapper.findByUserName(searchSysRole);
    }

    @Override
    public List<SysUserRole> findUserRoleList(Long userId) {
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return sysUserRoleMapper.selectByExample(example);
    }
}
