package com.fantechs.security.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysRoleExcelDTO;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.SysUserRole;
import com.fantechs.common.base.entity.security.history.SysHtRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
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
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleCode",sysRole.getRoleCode());
        List<SysRole> sysRoles = sysRoleMapper.selectByExample(example);
        if(null!=sysRoles&&sysRoles.size()>0){
            return ErrorCodeEnum.OPT20012001.getCode();
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
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }
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
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        for (Long roleId : roleIds) {
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
            if(StringUtils.isEmpty(sysRole)){
                //throw new BizErrorException("该角色已被删除。");
                return ErrorCodeEnum.OPT20012003.getCode();
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
    public List<SysUser> findBindUser(String searchStr, Long roleId) {
        return sysRoleMapper.findBindUser(searchStr,roleId);
    }

    @Override
    public List<SysUser> findUnBindUser(String searchStr, Long roleId) {
        return sysRoleMapper.findUnBindUser(searchStr,roleId);
    }


    @Override
    public List<SysRoleExcelDTO> selectRolesExcelDto(SearchSysRole searchSysRole) {
        List<SysRole> sysRoles = this.selectRoles(searchSysRole);
        List<SysRoleExcelDTO> dtoList=new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            SysRoleExcelDTO sysRoleExcelDTO =new SysRoleExcelDTO();
            try {
                BeanUtils.copyProperties(sysRole, sysRoleExcelDTO);
                dtoList.add(sysRoleExcelDTO);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dtoList;
    }

    @Override
    public int addUser(Long roleId, List<Long> userIds) {
        List<SysUserRole> list=new ArrayList<>();

        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

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
