package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysOrganizationUser;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.SysUserRole;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.general.entity.basic.history.BaseHtOrganization;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtOrganizationMapper;
import com.fantechs.provider.base.mapper.BaseOrganizationMapper;
import com.fantechs.provider.base.service.BaseOrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/12/29.
 */
@Service
public class BaseOrganizationServiceImpl extends BaseService<BaseOrganization> implements BaseOrganizationService {

    @Resource
    private BaseOrganizationMapper baseOrganizationMapper;
    @Resource
    private BaseHtOrganizationMapper baseHtOrganizationMapper;


    @Override
    public int save(BaseOrganization baseOrganization) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseOrganization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationCode",baseOrganization.getOrganizationCode());
        BaseOrganization baseOrganization1 = baseOrganizationMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseOrganization1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseOrganization.setCreateUserId(user.getUserId());
        baseOrganization.setCreateTime(new Date());
        baseOrganization.setModifiedUserId(user.getUserId());
        baseOrganization.setModifiedTime(new Date());
        baseOrganization.setStatus(StringUtils.isEmpty(baseOrganization.getStatus())?1:baseOrganization.getStatus());
        int i = baseOrganizationMapper.insertUseGeneratedKeys(baseOrganization);

        BaseHtOrganization baseHtOrganization = new BaseHtOrganization();
        BeanUtils.copyProperties(baseOrganization,baseHtOrganization);
        baseHtOrganizationMapper.insert(baseHtOrganization);

        return i;
    }

    @Override
    public int update(BaseOrganization baseOrganization) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseOrganization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationCode",baseOrganization.getOrganizationCode())
                .andNotEqualTo("organizationId",baseOrganization.getOrganizationId());
        BaseOrganization baseOrganization1 = baseOrganizationMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseOrganization1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseOrganization.setModifiedTime(new Date());
        baseOrganization.setModifiedUserId(user.getUserId());

        BaseHtOrganization baseHtOrganization = new BaseHtOrganization();
        BeanUtils.copyProperties(baseOrganization,baseHtOrganization);
        baseHtOrganizationMapper.insert(baseHtOrganization);

        return baseOrganizationMapper.updateByPrimaryKeySelective(baseOrganization);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtOrganization> baseHtOrganizations = new LinkedList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseOrganization baseOrganization = baseOrganizationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseOrganization)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtOrganization baseHtOrganization = new BaseHtOrganization();
            BeanUtils.copyProperties(baseOrganization,baseHtOrganization);
            baseHtOrganizations.add(baseHtOrganization);
        }

        baseHtOrganizationMapper.insertList(baseHtOrganizations);
        return baseOrganizationMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseOrganizationDto> findList(Map<String, Object> map) {
        return baseOrganizationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUser(Long organizationId, List<Long> userIds) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseOrganizationMapper.deleteUserByOrganization(organizationId);

        List<SysOrganizationUser> list = new ArrayList<>();
        for (Long userId : userIds) {
            SysOrganizationUser sysOrganizationUser = new SysOrganizationUser();
            sysOrganizationUser.setOrganizationId(organizationId);
            sysOrganizationUser.setUserId(userId);
            list.add(sysOrganizationUser);
        }
        return baseOrganizationMapper.insertUser(list);
    }
}
