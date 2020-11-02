package com.fantechs.security.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysUserExcelDTO;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.history.SysHtUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysHtUserMapper;
import com.fantechs.security.mapper.SysUserMapper;
import com.fantechs.security.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class SysUserServiceImpl extends BaseService<SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysHtUserMapper sysHtUserMapper;


    @Override
    public List<SysUser> selectUsers(SearchSysUser searchSysUser) {
        return sysUserMapper.selectUsers(searchSysUser);
    }


    @Override
    public SysUser selectByCode(String userCode) {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userCode",userCode);
        SysUser smtUser = sysUserMapper.selectOneByExample(example);
        return smtUser;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysUser sysUser){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isNotEmpty(sysUser.getPassword())){
            sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        }
        sysUser.setCreateUserId(currentUser.getUserId());
        sysUser.setCreateTime(new Date());
        sysUser.setModifiedUserId(currentUser.getUserId());
        sysUser.setModifiedTime(new Date());

        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", sysUser.getUserName()).orEqualTo("userCode",sysUser.getUserCode());
        SysUser oneByUser = sysUserMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(oneByUser)){
            throw new BizErrorException("该用户的帐号/工号已存在。");
        }

        int i =sysUserMapper.insertUseGeneratedKeys(sysUser);

        //新增用户历史信息
        SysHtUser sysHtUser=new SysHtUser();
        BeanUtils.copyProperties(sysUser,sysHtUser);
        sysHtUserMapper.insertSelective(sysHtUser);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysUser sysUser){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            //return ErrorCodeEnum.UAC10011039.getCode();
        }

        SysUser user = sysUserMapper.selectByPrimaryKey(sysUser.getUserId());
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }

        if(StringUtils.isNotEmpty(sysUser.getPassword())){
            sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        }
        sysUser.setModifiedUserId(currentUser.getUserId());
        sysUser.setModifiedTime(new Date());


        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", sysUser.getUserName()).orEqualTo("userCode",sysUser.getUserCode());
        SysUser oneByUser = sysUserMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(oneByUser)&&!sysUser.getUserId().equals(oneByUser.getUserId())){
            throw new BizErrorException("该用户的帐号/工号已存在。");
        }
        sysUserMapper.updateByPrimaryKeySelective(sysUser);

        //新增用户历史信息
        SysHtUser sysHtUser=new SysHtUser();
        BeanUtils.copyProperties(sysUser, sysHtUser);
        int i = sysHtUserMapper.insertSelective(sysHtUser);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String userIds) {
        int i=0;
        List<SysHtUser> list = new LinkedList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        String[] idsArr = userIds.split(",");
        for (String userId : idsArr) {
            SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
            if(StringUtils.isEmpty(sysUser)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                //return ErrorCodeEnum.OPT20012003.getCode();
            }

            //新增用户历史信息
            SysHtUser sysHtUser=new SysHtUser();
            BeanUtils.copyProperties(sysUser,sysHtUser);
            sysHtUser.setModifiedUserId(currentUser.getUserId());
            sysHtUser.setModifiedTime(new Date());
            list.add(sysHtUser);

            sysUserMapper.deleteByPrimaryKey(userId);
            i++;
        }

        sysHtUserMapper.insertList(list);
        return i;
    }

    @Override
    public List<SysUserExcelDTO> selectUsersExcelDto(SearchSysUser searchSysUser) {
        List<SysUser> smtUsers = this.selectUsers(searchSysUser);
        List<SysUserExcelDTO> dtoList=new ArrayList<>();
        for (SysUser sysUser : smtUsers) {
            SysUserExcelDTO smtUserExcelDTO =new SysUserExcelDTO();
            try {
                BeanUtils.copyProperties(sysUser, smtUserExcelDTO);
                dtoList.add(smtUserExcelDTO);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importUsers(List<SysUser> sysUsers) {
        int i = 0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        List<SysUser> list = new LinkedList<>();
        List<SysHtUser> htUsers = new LinkedList<>();
        for (SysUser sysUser : sysUsers) {
            if (StringUtils.isEmpty(sysUser.getUserCode(), sysUser.getUserName(),
                    sysUser.getNickName(), sysUser.getPassword())) {
                continue;
            }

            Example example = new Example(SysUser.class);
            Example.Criteria criteria = example.createCriteria();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andNotEqualTo("userId",sysUser.getUserId());
            criteria1.andEqualTo("status",1).orIsNull("status");
            example.and(criteria1);
            criteria.andEqualTo("userName", sysUser.getUserName()).orEqualTo("userCode",sysUser.getUserCode());
            SysUser oneByUser = sysUserMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(oneByUser)) {
                continue;
            }
            if(StringUtils.isNotEmpty(sysUser.getPassword())){
                sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
            }

            String factoryName = sysUser.getFactoryName();
            String deptName = sysUser.getDeptName();
            if(StringUtils.isNotEmpty(factoryName)&&StringUtils.isNotEmpty(deptName)){
                SmtDept smtDept = sysUserMapper.selectDept(factoryName,deptName);

                if(StringUtils.isNotEmpty(smtDept)){
                    //导入厂别
                    sysUser.setFactoryId(smtDept.getFactoryId());
                    //导入部门
                    sysUser.setDeptId(smtDept.getDeptId());
                }
            }

            sysUser.setCreateUserId(currentUser.getUserId());
            sysUser.setCreateTime(new Date());
            sysUser.setIsDelete((byte) 1);
            list.add(sysUser);
        }
        if (StringUtils.isNotEmpty(list)) {
            i = sysUserMapper.insertList(list);
        }

        for (SysUser sysUser : list) {
            //新增用户历史信息
            SysHtUser sysHtUser=new SysHtUser();
            BeanUtils.copyProperties(sysUser,sysHtUser);
            htUsers.add(sysHtUser);
        }
        if (StringUtils.isNotEmpty(list)) {
            sysHtUserMapper.insertList(htUsers);
        }
        return  i;
    }

}
