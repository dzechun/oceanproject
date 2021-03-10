package com.fantechs.security.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.security.SysUserExcelDTO;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtFactory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.history.SysHtUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.security.mapper.SysHtUserMapper;
import com.fantechs.security.mapper.SysUserMapper;
import com.fantechs.security.service.SysUserService;
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
    private BasicFeignApi basicFeignApi;


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

        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", sysUser.getUserName()).orEqualTo("userCode",sysUser.getUserCode());
        SysUser oneByUser = sysUserMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(oneByUser)){
            throw new BizErrorException("该用户的帐号/工号已存在。");
        }

        sysUser.setCreateUserId(currentUser.getUserId());
        sysUser.setCreateTime(new Date());
        sysUser.setModifiedUserId(currentUser.getUserId());
        sysUser.setModifiedTime(new Date());
        sysUser.setIsDelete(StringUtils.isEmpty(sysUser.getIsDelete())?(byte)0:sysUser.getIsDelete());
        sysUser.setStatus(StringUtils.isEmpty(sysUser.getStatus())?1:sysUser.getStatus());

        sysUserMapper.insertUseGeneratedKeys(sysUser);

        //新增用户历史信息
        SysHtUser sysHtUser=new SysHtUser();
        BeanUtils.copyProperties(sysUser,sysHtUser);

        return  sysHtUserMapper.insertSelective(sysHtUser);
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
                SearchSmtFactory searchSmtFactory = new SearchSmtFactory();
                searchSmtFactory.setCodeQueryMark((byte) 1);
                searchSmtFactory.setFactoryCode(factoryCode);
                List<SmtFactoryDto> smtFactoryDtos = basicFeignApi.findFactoryList(searchSmtFactory).getData();
                if (StringUtils.isEmpty(smtFactoryDtos)){
                    fail.add(i+4);
                    continue;
                }
                sysUserExcelDTO.setFactoryId(smtFactoryDtos.get(0).getFactoryId());
            }

            //如果部门编码不为空则判断部门信息是否存在
            if (StringUtils.isNotEmpty(deptCode)){
                SearchSmtDept searchSmtDept = new SearchSmtDept();
                searchSmtDept.setCodeQueryMark(1);
                searchSmtDept.setDeptCode(deptCode);
                List<SmtDept> smtDepts = basicFeignApi.selectDepts(searchSmtDept).getData();
                if (StringUtils.isEmpty(smtDepts)){
                    fail.add(i+4);
                    continue;
                }
                sysUserExcelDTO.setDeptId(smtDepts.get(0).getDeptId());
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
                sysUser.setIsDelete((byte) 1);
                sysUser.setStatus(1);
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
    public List<String> findAllRoleId(Long userId) {
        return sysUserMapper.findAllRoleId(userId);
    }
}
