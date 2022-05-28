package com.fantechs.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.auth.mapper.SysHtUserMapper;
import com.fantechs.auth.mapper.SysUserMapper;
import com.fantechs.auth.mapper.SysUserRoleMapper;
import com.fantechs.auth.service.SysUserService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysUserExcelDTO;
import com.fantechs.common.base.entity.security.SysOrganizationUser;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.SysUserRole;
import com.fantechs.common.base.entity.security.history.SysHtUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFactory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.SnowFlakeUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.auth.mapper.SysOrganizationUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SysUserServiceImpl extends BaseService<SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysHtUserMapper sysHtUserMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SysOrganizationUserMapper sysOrganizationUserMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;



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
        if(StringUtils.isEmpty(sysUser.getPassword())){
            sysUser.setPassword("123456");
        }
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));

        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", sysUser.getUserName()).orEqualTo("userCode",sysUser.getUserCode());
        SysUser oneByUser = sysUserMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(oneByUser)){
            throw new BizErrorException("该用户的帐号/工号已存在。");
        }

        sysUser.setUserId(SnowFlakeUtil.getUid());
        sysUser.setCreateUserId(currentUser.getUserId());
        sysUser.setCreateTime(new Date());
        sysUser.setModifiedUserId(currentUser.getUserId());
        sysUser.setModifiedTime(new Date());
        sysUser.setIsDelete(StringUtils.isEmpty(sysUser.getIsDelete()) ? (byte)1 : sysUser.getIsDelete());
        sysUser.setStatus(StringUtils.isEmpty(sysUser.getStatus()) ? (byte)1 : sysUser.getStatus());
        sysUserMapper.insertUseGeneratedKeys(sysUser);

        //增加用户的角色、组织权限
        if(StringUtils.isNotEmpty(sysUser.getOrganizationId())) {
            SysOrganizationUser sysOrganizationUser = new SysOrganizationUser();
            sysOrganizationUser.setOrganizationId(sysUser.getOrganizationId());
            sysOrganizationUser.setUserId(sysUser.getUserId());
            sysOrganizationUserMapper.insert(sysOrganizationUser);
        }
        if(StringUtils.isNotEmpty(sysUser.getRoleId())) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getUserId());
            sysUserRole.setRoleId(sysUser.getRoleId());
            sysUserRoleMapper.insert(sysUserRole);
        }
        //新增用户历史信息
        SysHtUser sysHtUser=new SysHtUser();
        BeanUtils.copyProperties(sysUser,sysHtUser);
        return  sysHtUserMapper.insertSelective(sysHtUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysUser sysUser){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        SysUser user = sysUserMapper.selectByPrimaryKey(sysUser.getUserId());
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }

        //修改用户绑定角色,只更新未绑定或绑定一个角色的用户
     /*   Example roleExample = new Example(SysUserRole.class);
        Example.Criteria roleCriteria = roleExample.createCriteria();
        roleCriteria.andEqualTo("userId",user.getUserId());
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByExample(roleExample);
        if(sysUserRoles.size()<=1) {
            sysUserRoleMapper.deleteByExample(roleExample);
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getUserId());
            sysUserRole.setRoleId(sysUser.getRoleId());
            sysUserRoleMapper.insert(sysUserRole);
        }*/

        if(StringUtils.isNotEmpty(sysUser.getPassword())){
            sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        }
        sysUser.setModifiedUserId(currentUser.getUserId());
        sysUser.setModifiedTime(new Date());
        sysUser.setDeptId(StringUtils.isEmpty(sysUser.getDeptId())?0:sysUser.getDeptId());

        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userCode",sysUser.getUserCode()).andNotEqualTo("userId", sysUser.getUserId());
        int byExample = sysUserMapper.selectCountByExample(example);

        if(byExample > 0){
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
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importUsers(List<SysUserExcelDTO> sysUserExcelDTOS) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        List<SysUser> list = new LinkedList<>();
        List<SysHtUser> htUsers = new LinkedList<>();
        ArrayList<SysUserExcelDTO> sysUserExcelDTOS1 = new ArrayList<>();

        for (int i = 0; i < sysUserExcelDTOS.size(); i++) {
            SysUserExcelDTO sysUserExcelDTO = sysUserExcelDTOS.get(i);

            String deptCode = sysUserExcelDTO.getDeptCode();
            String userCode = sysUserExcelDTO.getUserCode();
            String factoryCode = sysUserExcelDTO.getFactoryCode();
            String userName = sysUserExcelDTO.getUserName();
            String nickName = sysUserExcelDTO.getNickName();
            if (StringUtils.isEmpty(userCode,userName,nickName)) {
                fail.add(i+4);
                continue;
            }


            Example example = new Example(SysUser.class);
            Example.Criteria criteria = example.createCriteria();
            Example.Criteria criteria1 = example.createCriteria();
            criteria.andEqualTo("status",1).orIsNull("status");
            criteria1.andEqualTo("userName", sysUserExcelDTO.getUserName())
                    .orEqualTo("userCode",sysUserExcelDTO.getUserCode());
            example.and(criteria1);
            List<SysUser> sysUsers1 = sysUserMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(sysUsers1)) {
                fail.add(i+4);
                continue;
            }

            //如果工厂编码不为空则判断厂别信息是否存在
            if (StringUtils.isNotEmpty(factoryCode)){
                SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
                searchBaseFactory.setCodeQueryMark((byte) 1);
                searchBaseFactory.setFactoryCode(factoryCode);
                List<BaseFactoryDto> smtFactoryDtos = baseFeignApi.findFactoryList(searchBaseFactory).getData();
                if (StringUtils.isEmpty(smtFactoryDtos)){
                    fail.add(i+4);
                    continue;
                }
                sysUserExcelDTO.setFactoryId(smtFactoryDtos.get(0).getFactoryId());
            }

            //如果部门编码不为空则判断部门信息是否存在
            if (StringUtils.isNotEmpty(deptCode)){
                SearchBaseDept searchBaseDept = new SearchBaseDept();
                searchBaseDept.setCodeQueryMark(1);
                searchBaseDept.setDeptCode(deptCode);
                List<BaseDept> baseDepts = baseFeignApi.selectDepts(searchBaseDept).getData();
                if (StringUtils.isEmpty(baseDepts)){
                    fail.add(i+4);
                    continue;
                }
                sysUserExcelDTO.setDeptId(baseDepts.get(0).getDeptId());
            }

            //判断集合中是否已经存在该用户
            boolean tag = false;
            if (StringUtils.isNotEmpty(sysUserExcelDTOS1)){
                for (SysUserExcelDTO userExcelDTO : sysUserExcelDTOS1) {
                    if (userExcelDTO.getUserCode().equals(userCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            sysUserExcelDTOS1.add(sysUserExcelDTO);
        }

        if (StringUtils.isNotEmpty(sysUserExcelDTOS1)){
            for (SysUserExcelDTO sysUserExcelDTO : sysUserExcelDTOS1) {
                SysUser sysUser=new SysUser();
                BeanUtils.copyProperties(sysUserExcelDTO,sysUser);
                if(StringUtils.isNotEmpty(sysUserExcelDTO.getPassword())){
                    sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUserExcelDTO.getPassword()));
                }
                sysUser.setCreateUserId(currentUser.getUserId());
                sysUser.setCreateTime(new Date());
                sysUser.setIsDelete((byte)1);
                sysUser.setStatus((byte)1);
                list.add(sysUser);
            }

            if (StringUtils.isNotEmpty(list)) {
                success = sysUserMapper.insertList(list);
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
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public int switchOrganization(Long organizationId) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<Long> organizationList = sysUserMapper.findOrganizationList(currentUser.getUserId());
        if (!organizationList.contains(organizationId)) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"该组织未找到当前用户");
        }
        SysUser sysUser = JSON.parseObject(JSON.toJSONString(redisUtil.get(CurrentUserInfoUtils.getToken())), SysUser.class);
        sysUser.setOrganizationId(organizationId);
        redisUtil.set(CurrentUserInfoUtils.getToken(),sysUser);
        return 1;
    }

    @Override
    public List<String> findAllRoleId(Long userId) {
        return sysUserMapper.findAllRoleId(userId);
    }

    @Override
    public int updatePassword(String oldPassword, String newPassword) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (!new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())){
            throw new BizErrorException("旧密码不正确");
        }
        Example example = new Example(SysUser.class);
        example.createCriteria().andEqualTo("userId",user.getUserId());
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

        return sysUserMapper.updateByExampleSelective(user,example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser saveByApi(SysUser sysUser) {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userCode", sysUser.getUserCode());
        SysUser oneByUser = sysUserMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(oneByUser)){
            sysUser.setModifiedTime(new Date());
            sysUser.setUserId(oneByUser.getUserId());
            sysUserMapper.updateByPrimaryKeySelective(sysUser);
        }
        else{
            sysUser.setUserId(SnowFlakeUtil.getUid());
            sysUser.setCreateUserId(1L);
            sysUser.setCreateTime(new Date());
            sysUser.setModifiedUserId(1L);
            sysUser.setModifiedTime(new Date());
            sysUser.setIsDelete(StringUtils.isEmpty(sysUser.getIsDelete()) ? (byte)1 : sysUser.getIsDelete());
            sysUser.setStatus(StringUtils.isEmpty(sysUser.getStatus()) ? (byte)1 : sysUser.getStatus());
            sysUserMapper.insertUseGeneratedKeys(sysUser);

            //增加用户的角色、组织权限
            if(StringUtils.isNotEmpty(sysUser.getOrganizationId())) {
                SysOrganizationUser sysOrganizationUser = new SysOrganizationUser();
                sysOrganizationUser.setOrganizationId(sysUser.getOrganizationId());
                sysOrganizationUser.setUserId(sysUser.getUserId());
                sysOrganizationUserMapper.insert(sysOrganizationUser);
            }
            if(StringUtils.isNotEmpty(sysUser.getRoleId())) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getUserId());
                sysUserRole.setRoleId(sysUser.getRoleId());
                sysUserRoleMapper.insert(sysUserRole);
            }
            //新增用户历史信息
            SysHtUser sysHtUser=new SysHtUser();
            BeanUtils.copyProperties(sysUser,sysHtUser);
            sysHtUserMapper.insertSelective(sysHtUser);
        }

        return sysUser;
    }
}
